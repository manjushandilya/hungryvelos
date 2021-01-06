package com.velokofi.hungryvelos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.reverseOrder;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

public class SerializeTest {

    @Test
    public void readSingleActivity() throws Exception {
        String json = """
                    {
                        "resource_state": 2,
                        "clubActivityAthlete": {
                            "resource_state": 2,
                            "firstname": "Ravi",
                            "lastname": "S."
                        },
                        "name": "Morning Ride",
                        "distance": 17779.7,
                        "moving_time": 4312,
                        "elapsed_time": 8302,
                        "total_elevation_gain": 101.3,
                        "type": "Ride",
                        "workout_type": null
                    }
                """;

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        ClubMemberActivity clubMemberActivity = mapper.readValue(json, ClubMemberActivity.class);
        System.out.println(clubMemberActivity);
    }

    @Test
    public void readMultipleActivities() throws Exception {
        String json = """
                   [
                       {
                             "resource_state": 2,
                             "clubActivityAthlete": {
                                 "resource_state": 2,
                                 "firstname": "Chandrakanth",
                                 "lastname": "K."
                             },
                             "name": "Morning Ride",
                             "distance": 35392.2,
                             "moving_time": 8001,
                             "elapsed_time": 10896,
                             "total_elevation_gain": 207.4,
                             "type": "Ride",
                             "workout_type": 10
                         },
                         {
                             "resource_state": 2,
                             "clubActivityAthlete": {
                                 "resource_state": 2,
                                 "firstname": "Sukumar",
                                 "lastname": "S."
                             },
                             "name": "Velokofi Launch",
                             "distance": 50211.2,
                             "moving_time": 12810,
                             "elapsed_time": 21387,
                             "total_elevation_gain": 401.7,
                             "type": "Ride",
                             "workout_type": 12
                         }
                   ]
                """;

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        ClubMemberActivity[] activities = mapper.readValue(json, ClubMemberActivity[].class);
        System.out.println(activities.length);
        Stream.of(activities).forEach(System.out::println);
    }

    @Test
    public void readAthleteNames() throws Exception {
        Set<ClubMemberActivity> activities = getAthleteActivities();
        Set<ClubActivityAthlete> clubActivityAthletes = new HashSet<>();
        for (ClubMemberActivity clubMemberActivity : activities) {
            if (!clubActivityAthletes.contains(clubMemberActivity.getAthlete())) {
                clubActivityAthletes.add(clubMemberActivity.getAthlete());
            }
        }
        clubActivityAthletes.stream().map(a -> a.getFirstname() + " " + a.getLastname())
                .collect(Collectors.toSet()).stream().sorted(String::compareToIgnoreCase).forEach(System.out::println);
    }

    @Test
    public void readMemberNames() throws Exception {
        ClubMember[] clubMembers = getClubMembers();
        Stream.of(clubMembers).map(m -> m.getFirstname() + " " + m.getLastname())
                .collect(Collectors.toSet()).stream().sorted(String::compareToIgnoreCase).forEach(System.out::println);
    }

    @Test
    public void readTeams() throws Exception {
        Team[] teams = getTeams();
        Stream.of(teams).forEach(System.out::println);
    }

    @Test
    public void testTopTeams() throws Exception {
        Team[] teams = getTeams();
        Set<ClubMemberActivity> athleteActivities = getAthleteActivities();

        Map<String, Double> athleteTotals = athleteActivities.stream()
                .collect(
                        groupingBy(
                                a -> a.getAthleteName(),
                                summingDouble(a -> a.getDistance() / 1000)
                        )
                );
        Stream<Entry<String, Double>> athleteTotalsSorted = athleteTotals.entrySet().stream().sorted(comparingByValue(reverseOrder()));
        athleteTotalsSorted.forEach(System.out::println);

        Map<String, Double> teamTotals = Arrays.stream(teams)
                .collect(
                        groupingBy(
                                t -> t.getName(),
                                summingDouble(t -> getAthleteDistance(t, athleteTotals))
                        )
                );
        Stream<Entry<String, Double>> teamTotalsSorted = teamTotals.entrySet().stream().sorted(comparingByValue(reverseOrder()));
        teamTotalsSorted.forEach(System.out::println);
    }

    private double getAthleteDistance(Team team, Map<String, Double> map) {
        List<TeamMember> members = team.getMembers();
        double total = 0;
        for (TeamMember member : members) {
            if (map.containsKey(member.getName())) {
                total += map.get(member.getName());
            }
        }
        return total;
    }

    @Test
    public void testMrAlemaari() throws Exception {
        Team[] teams = getTeams();

        Set<ClubMemberActivity> athleteActivities = getAthleteActivities();
        Map<String, Double> map = athleteActivities.stream()
                .filter(a -> filterBasedOnGender(a.getAthlete(), teams, "M"))
                .collect(groupingBy(
                        a -> a.getAthleteName(),
                        summingDouble(a -> a.getDistance() / 1000))
                );

        map.entrySet().stream().sorted(comparingByValue(reverseOrder())).forEach(System.out::println);
    }

    @Test
    public void testMsAlemaari() throws Exception {
        Team[] teams = getTeams();

        Set<ClubMemberActivity> athleteActivities = getAthleteActivities();
        Map<String, Double> map = athleteActivities.stream()
                .filter(a -> filterBasedOnGender(a.getAthlete(), teams, "F"))
                .collect(groupingBy(
                        a -> a.getAthleteName(),
                        summingDouble(a -> a.getDistance() / 1000))
                );

        map.entrySet().stream().sorted(comparingByValue(reverseOrder())).forEach(System.out::println);
    }

    @Test
    public void testMrBettappa() throws Exception {
        Team[] teams = getTeams();

        Set<ClubMemberActivity> athleteActivities = getAthleteActivities();
        Map<String, Double> map = athleteActivities.stream()
                .filter(a -> filterBasedOnGender(a.getAthlete(), teams, "M"))
                .collect(groupingBy(
                        a -> a.getAthleteName(),
                        summingDouble(a -> a.getTotal_elevation_gain()))
                );

        map.entrySet().stream().sorted(comparingByValue(reverseOrder())).forEach(System.out::println);
    }

    @Test
    public void testMsBettamma() throws Exception {
        Team[] teams = getTeams();

        Set<ClubMemberActivity> athleteActivities = getAthleteActivities();
        Map<String, Double> map = athleteActivities.stream()
                .filter(a -> filterBasedOnGender(a.getAthlete(), teams, "F"))
                .collect(groupingBy(
                        a -> a.getAthleteName(),
                        summingDouble(a -> a.getTotal_elevation_gain()))
                );

        map.entrySet().stream().sorted(comparingByValue(reverseOrder())).forEach(System.out::println);
    }

    private boolean filterBasedOnGender(ClubActivityAthlete clubActivityAthlete, Team[] teams, String gender) {
        Set<TeamMember> teamMembers = Stream.of(teams).flatMap(team -> team.getMembers().stream()).collect(Collectors.toSet());
        Optional<TeamMember> optional = teamMembers.stream().filter(
                teamMember -> teamMember.getName().equals(clubActivityAthlete.getAthleteName()) &&
                        gender.equals(teamMember.getGender())).findFirst();
        return optional.isPresent();
    }

    private ClubMember[] getClubMembers() throws Exception {
        Path path = Paths.get("src", "test", "resources", "clubMembers.json");
        String json = Files.readString(path);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        ClubMember[] clubMembers = mapper.readValue(json, ClubMember[].class);
        return clubMembers;
    }

    private Set<ClubMemberActivity> getAthleteActivities() throws IOException {
        Path path = Paths.get("src", "test", "resources", "clubActivities.json");
        String json = Files.readString(path);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        ClubMemberActivity[] activities = mapper.readValue(json, ClubMemberActivity[].class);
        return Stream.of(activities).filter(activity -> activity.getType().equals("Ride")).collect(Collectors.toSet());
    }

    private Team[] getTeams() throws IOException {
        Path path = Paths.get("src", "test", "resources", "teams.json");
        String json = Files.readString(path);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        Team[] teams = mapper.readValue(json, Team[].class);
        return teams;
    }



}
