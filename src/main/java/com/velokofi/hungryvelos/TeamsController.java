package com.velokofi.hungryvelos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public final class TeamsController implements TeamConstants {

    private TeamsRepository repository;

    public TeamsController() {
        repository = new TeamsRepository();
    }

    @GetMapping("/teams")
    public String teams() throws Exception {
        final List<Team> teams = repository.listTeams();
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(teams);
    }

    @GetMapping("/teamMembers")
    public final String teamMembers() throws Exception {
        List<TeamMember> teamMembers = repository.listTeamMembers();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(teamMembers);
    }

}
