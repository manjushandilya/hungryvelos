package com.velokofi.hungryvelos.controller;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.*;
import com.velokofi.hungryvelos.persistence.PersistenceManager;
import com.velokofi.hungryvelos.persistence.TeamsRepository;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.*;

@RestController
public final class LeaderBoardController {

    public static final int INDIVIDUAL_TARGET = 250;

    public enum MetricType {DISTANCE, ELEVATION}

    private final TeamsRepository teamsRepository;

    private final RestTemplate restTemplate;

    public LeaderBoardController(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.teamsRepository = new TeamsRepository();
    }

    @GetMapping("/")
    public ModelAndView leaderboard(@RegisteredOAuth2AuthorizedClient final OAuth2AuthorizedClient client,
                                    @RequestParam(required = false, defaultValue = "false") boolean debug) throws Exception {

        PersistenceManager.persistClient(client);

        final LeaderBoard leaderBoard = new LeaderBoard();
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final String tokenValue = client.getAccessToken().getTokenValue();

        final String profileResponse = getResponse(tokenValue, "https://www.strava.com/api/v3/athlete");
        final AthleteProfile athleteProfile = mapper.readValue(profileResponse, AthleteProfile.class);

        leaderBoard.setAthleteProfile(athleteProfile);

        for (int page = 1; ; page++) {
            final StringBuilder url = new StringBuilder();
            url.append("https://www.strava.com/api/v3/athlete/activities");
            url.append("?per_page=200");
            url.append("&after=").append("1609631999"); // Start of 3 Jan 2021
            url.append("&page=").append(page);

            if (debug) {
                System.out.println("Hitting url: " + url);
            }

            String activitiesResponse = getResponse(tokenValue, url.toString());

            if (debug) {
                System.out.println(activitiesResponse);
            }

            AthleteActivity[] activitiesArray = mapper.readValue(activitiesResponse, AthleteActivity[].class);
            Stream.of(activitiesArray).forEach(activity -> PersistenceManager.persistActivity(activity));
            if (activitiesArray.length < 200) {
                break;
            }
        }

        final List<AthleteActivity> activities = PersistenceManager.retrieveActivities();

        final List<Team> teams = teamsRepository.listTeams();
        { // event totals
            final Double totalDistance = round(activities.stream().collect(summingDouble(a -> a.getDistance())) / 1000);

            final Double totalElevation = round(activities.stream().collect(summingDouble(a -> a.getTotal_elevation_gain())));

            final int totalRides = activities.size();

            final long movingTimeInSeconds = activities.stream().collect(summingLong(a -> a.getMoving_time()));
            final long movingTimeInHours = movingTimeInSeconds / 3600;

            leaderBoard.setTotalDistance(totalDistance);
            leaderBoard.setTotalElevation(totalElevation);
            leaderBoard.setTotalRides(totalRides);
            leaderBoard.setMovingTime(movingTimeInHours);
            leaderBoard.setMovingTimeInHumanReadableFormat(humanReadableFormat(Duration.ofSeconds(movingTimeInSeconds)));

            final int memberCount = teams.stream().collect(summingInt(t -> t.getMembers().size()));
            leaderBoard.setRiderAverage(round(totalDistance / memberCount));
            leaderBoard.setRiderCount(memberCount);
        }

        {
            // Calculate athlete distance
            final Map<Long, Double> athleteDistanceMap = activities.stream().collect(
                    groupingBy(a -> a.getAthlete().getId(), summingDouble(a -> a.getDistance() / 1000))
            );
            System.out.println("athleteDistanceMap: " + athleteDistanceMap);

            // Calculate team distance
            final Map<String, Double> teamDistanceMap = teams.stream().collect(
                    groupingBy(t -> t.getName(), summingDouble(t -> getAthleteAggregate(t, athleteDistanceMap)))
            );
            System.out.println("teamDistanceMap: " + teamDistanceMap);
            leaderBoard.setTeamDistanceMap(teamDistanceMap);

            // Calculate team average distance
            final Map<String, Double> teamAvgDistanceMap = teamDistanceMap.entrySet().stream().collect(
                    toMap(
                            e -> e.getKey(), e -> e.getValue() / getTeamMemberCount(e.getKey(), teams)
                    )
            );
            System.out.println("teamAvgDistanceMap: " + teamAvgDistanceMap);
            leaderBoard.setTeamAvgDistanceMap(teamAvgDistanceMap);

            // Calculate athlete elevation
            final Map<Long, Double> athleteElevationMap = activities.stream().collect(
                    groupingBy(a -> a.getAthlete().getId(), summingDouble(a -> a.getTotal_elevation_gain()))
            );
            System.out.println("athleteElevationMap: " + athleteElevationMap);

            // Calculate team elevation
            final Map<String, Double> teamElevationMap = teams.stream().collect(
                    groupingBy(t -> t.getName(), summingDouble(t -> getAthleteAggregate(t, athleteElevationMap)))
            );
            System.out.println("teamElevationMap: " + teamElevationMap);
            leaderBoard.setTeamElevationMap(teamElevationMap);

            // Calculate team average elevation
            final Map<String, Double> teamAvgElevationMap = teamElevationMap.entrySet().stream().collect(
                    toMap(
                            e -> e.getKey(), e -> e.getValue() / getTeamMemberCount(e.getKey(), teams)
                    )
            );
            System.out.println("teamAvgElevationMap: " + teamAvgElevationMap);
            leaderBoard.setTeamAvgElevationMap(teamAvgElevationMap);

            // Calculate athlete ride count
            final Map<Long, Double> athleteRideCountMap = activities.stream().collect(
                    groupingBy(a -> a.getAthlete().getId(), summingDouble(a -> 1D))
            );
            System.out.println("athleteRideCountMap: " + athleteRideCountMap);

            // Calculate team ride count
            final Map<String, Double> teamRideCountMap = teams.stream().collect(
                    groupingBy(t -> t.getName(), summingDouble(t -> getAthleteAggregate(t, athleteRideCountMap)))
            );
            System.out.println("teamRideCountMap: " + teamRideCountMap);
            leaderBoard.setTeamRidesMap(teamRideCountMap);

            // Calculate team average ride count
            final Map<String, Double> teamAvgRidesMap = teamRideCountMap.entrySet().stream().collect(
                    toMap(
                            e -> e.getKey(), e -> e.getValue() / getTeamMemberCount(e.getKey(), teams)
                    )
            );
            System.out.println("teamAvgRidesMap: " + teamAvgRidesMap);
            leaderBoard.setTeamAvgRidesMap(teamAvgRidesMap);
        }

        leaderBoard.setMrAlemaari(aggregate(activities, teams, "M", MetricType.DISTANCE));
        leaderBoard.setMsAlemaari(aggregate(activities, teams, "F", MetricType.DISTANCE));

        leaderBoard.setBettappa(aggregate(activities, teams, "M", MetricType.ELEVATION));
        leaderBoard.setBettamma(aggregate(activities, teams, "F", MetricType.ELEVATION));

        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("leaderBoard", leaderBoard);
        mav.addObject("principalName", client.getPrincipalName());
        return mav;
    }

    private long getTeamMemberCount(final String teamName, final List<Team> teams) {
        final Optional<Team> optional = teams.stream().filter(t -> t.getName().equals(teamName)).findFirst();
        if (optional.isPresent()) {
            return optional.get().getMembers().size();
        }
        return 0;
    }

    private List<Entry<String, Double>> aggregate(final List<AthleteActivity> activities,
                                                  final List<Team> teams,
                                                  final String gender,
                                                  final MetricType metricType) {
        final Map<String, Double> aggregateMap = activities.stream()
                .filter(a -> filterBasedOnGender(a.getAthlete(), teams, gender))
                .collect(groupingBy(
                        a -> teamsRepository.getNameForId(a.getAthlete().getId()),
                        summingDouble(a -> round(metricType == MetricType.DISTANCE ? a.getDistance() / 1000 : a.getTotal_elevation_gain())))
                );

        final Stream<Entry<String, Double>> aggregateSorted = aggregateMap.entrySet().stream().sorted(comparingByValue(reverseOrder()));
        return aggregateSorted.collect(toList());
    }

    private String getResponse(final String tokenValue, final String url) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + tokenValue);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        final ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

    private boolean filterBasedOnGender(final AthleteActivity.Athlete athlete, final List<Team> teams, final String gender) {
        final Set<TeamMember> teamMembers = teams.stream().flatMap(team -> team.getMembers().stream()).collect(toSet());
        final Optional<TeamMember> optional = teamMembers.stream().filter(
                teamMember -> teamMember.getId() == athlete.getId() && gender.equals(teamMember.getGender())).findFirst();
        return optional.isPresent();
    }

    private double getAthleteAggregate(final Team team, final Map<Long, Double> map) {
        final List<TeamMember> members = team.getMembers();
        double total = 0;
        for (final TeamMember member : members) {
            if (map.containsKey(member.getId())) {
                total += map.get(member.getId());
            }
        }
        return total;
    }

    private double round(final double val) {
        return new BigDecimal(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static String humanReadableFormat(final Duration duration) {
        return String.format("%sd %sh %sm", duration.toDays(),
                duration.toHours() - TimeUnit.DAYS.toHours(duration.toDays()),
                duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours()),
                duration.getSeconds() - TimeUnit.MINUTES.toSeconds(duration.toMinutes()));
    }

}
