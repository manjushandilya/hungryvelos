package com.velokofi.hungryvelos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.AthleteActivity;
import com.velokofi.hungryvelos.model.AuthorizedClient;
import com.velokofi.hungryvelos.persistence.AthleteActivityRepository;
import com.velokofi.hungryvelos.persistence.AuthorizedClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacheController {

    @Autowired
    private AthleteActivityRepository athleteActivityRepo;

    @Autowired
    private AuthorizedClientRepository authorizedClientRepo;

    @GetMapping("/cache/activities")
    public String getActivities() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final List<AthleteActivity> activities = athleteActivityRepo.findAll();
        return mapper.writeValueAsString(activities);
    }

    @GetMapping("/cache/clients")
    public String getClients() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final List<AuthorizedClient> clients = authorizedClientRepo.findAll();
        return mapper.writeValueAsString(clients);
    }

    @GetMapping("/cache")
    public String operation(@RequestParam(name = "action") String action) throws Exception {
        switch (action) {
            case "clear":
                athleteActivityRepo.deleteAll();
                authorizedClientRepo.deleteAll();
                break;
        }
        return "";
    }

}
