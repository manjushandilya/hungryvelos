package com.velokofi.hungryvelos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.AthleteActivity;
import com.velokofi.hungryvelos.persistence.PersistenceManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacheController {

    @GetMapping("/cache/activities")
    public String getActivities() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final List<AthleteActivity> activities = PersistenceManager.retrieveActivities();
        return mapper.writeValueAsString(activities);
    }

    @GetMapping("/cache/clients")
    public String getClients() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        final List<OAuth2AuthorizedClient> clients = PersistenceManager.retrieveClients();
        return mapper.writeValueAsString(clients);
    }

    @GetMapping("/cache")
    public String operation(@RequestParam(name = "action") String action) throws Exception {
        switch (action) {
            case "clear":
                PersistenceManager.deleteAll();
                break;
        }
        return "";
    }

}
