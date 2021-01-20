package com.velokofi.hungryvelos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.AthleteActivity;
import com.velokofi.hungryvelos.model.Team;
import com.velokofi.hungryvelos.model.TeamMember;
import com.velokofi.hungryvelos.persistence.TeamsRepository;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import static com.velokofi.hungryvelos.util.NumberCruncher.summingAggregateLong;
import static java.util.stream.Collectors.toList;

public class ActivityCountTest {

    @Test
    public void test() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final List<AthleteActivity> allActivities = new ArrayList<>();
        final Path activitiesDir = Paths.get("src","test", "resources", "activities");
        final File[] files = activitiesDir.toFile().listFiles();
        for (final File file: files) {
            final byte[] bytes = Files.readAllBytes(file.toPath());
            final AthleteActivity[] activities = mapper.readValue(bytes, AthleteActivity[].class);
            allActivities.addAll(Arrays.asList(activities));
        }

        System.out.println("allActivities.size(): " + allActivities.size());

        final TeamsRepository teamsRepository = new TeamsRepository();

        final List<Team> teams = teamsRepository.listTeams();
        final List<TeamMember> teamMembers = teams.stream().flatMap(t -> t.getMembers().stream()).collect(toList());
        System.out.println("teamMembers: " + teamMembers);

        final List<Entry<String, Long>> entries = summingAggregateLong(allActivities, teamMembers, "F");

        System.out.println("only female activities: " + entries);
    }

}
