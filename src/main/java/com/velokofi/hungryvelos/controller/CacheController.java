package com.velokofi.hungryvelos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.AthleteActivity;
import com.velokofi.hungryvelos.repository.PersistenceManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CacheController {

    @GetMapping("/cache/clients")
    public String getClients() throws Exception {
        final List<OAuth2AuthorizedClient> clients = PersistenceManager.retrieveClients();
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(clients);
    }

    @GetMapping("/cache/activities")
    public String getActivities() throws Exception {
        final List<AthleteActivity> activities = PersistenceManager.retrieveActivities();
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(activities);
    }

    @DeleteMapping("/cache")
    public String deleteAll() throws Exception {
        PersistenceManager.deleteAll();
        return "";
    }

}
