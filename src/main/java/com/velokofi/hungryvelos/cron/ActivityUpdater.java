package com.velokofi.hungryvelos.cron;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.AthleteActivity;
import com.velokofi.hungryvelos.model.OAuthorizedClient;
import com.velokofi.hungryvelos.model.RefreshTokenRequest;
import com.velokofi.hungryvelos.model.RefreshTokenResponse;
import com.velokofi.hungryvelos.persistence.AthleteActivityRepository;
import com.velokofi.hungryvelos.persistence.OAuthorizedClientRepository;
import com.velokofi.hungryvelos.persistence.Saver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Component
public final class ActivityUpdater {

    @Autowired
    private AthleteActivityRepository athleteActivityRepo;

    @Autowired
    private OAuthorizedClientRepository oAuthClientRepo;

    @Scheduled(fixedDelay = 60 * 1000 * 60, initialDelay = 60 * 1000 * 5)
    public void run() throws Exception {
        System.out.println("Running scheduled task at: " + LocalDateTime.now());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final List<OAuthorizedClient> clients = oAuthClientRepo.findAll();
        final List<String> clientIds = clients.stream().map(c -> c.getPrincipalName()).collect(toList());

        for (final String clientId : clientIds) {
            //System.out.println("Fetching activities for client with id: " + clientId);
            int pageNumber = 1;
            for (int retries = 0; retries < 10; retries++) {
                final URI uri = getUri(pageNumber);
                final RestTemplate restTemplate = new RestTemplate();
                final HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + getTokenValue(clientId));
                final HttpEntity<String> request = new HttpEntity<String>(headers);

                try {
                    //System.out.println("Trying to fetch activities with pageNumber: " + pageNumber);
                    final ResponseEntity<String> activitiesResponse = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
                    final AthleteActivity[] activitiesArray = mapper.readValue(activitiesResponse.getBody(), AthleteActivity[].class);
                    if (activitiesArray.length > 0) {
                        //System.out.println("Saving " + activitiesArray.length + " activities to db");
                        Stream.of(activitiesArray)
                                .filter(a->a.getType().equalsIgnoreCase("ride"))
                                .forEach(activity -> athleteActivityRepo.save(activity));

                        //System.out.println("Saving " + activitiesArray.length + " activities to file");
                        Saver.persistActivities(clientId,activitiesResponse.getBody());
                    }

                    if (activitiesArray.length < 200) {
                        //System.out.println("Less than 200 (pageSize) activities found, breaking the loop...");
                        break;
                    }
                    pageNumber++;
                } catch (final Exception e) {
                    //System.out.println("Request failed with message: " + e.getMessage());
                    System.out.println("Refreshing auth token, old value: " + getTokenValue(clientId));

                    refresh(clientId);

                    //System.out.println("New value: " + getTokenValue(clientId));
                }
                //System.out.println("zZzZzZz ing for 5 seconds...");
                try {
                    Thread.sleep(5000);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void refresh(final String clientId) {
        final OAuthorizedClient client = oAuthClientRepo.findById(clientId).get();
        final OAuth2AuthorizedClient authorizedClient = OAuthorizedClient.fromBytes(client.getBytes());
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append("https://www.strava.com/api/v3/oauth/token");

            URI uri = new URI(builder.toString());

            final RestTemplate restTemplate = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            final RefreshTokenRequest requestObj = new RefreshTokenRequest();
            requestObj.setClient_id(authorizedClient.getClientRegistration().getClientId());
            requestObj.setClient_secret(authorizedClient.getClientRegistration().getClientSecret());
            requestObj.setGrant_type("refresh_token");
            requestObj.setRefresh_token(authorizedClient.getRefreshToken().getTokenValue());
            final String body = mapper.writeValueAsString(requestObj);

            //System.out.println("Refresh token request: " + body);

            final HttpEntity<String> request = new HttpEntity<String>(body, headers);

            final ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
            //System.out.println("Refresh token response: " + response);

            final RefreshTokenResponse refreshTokenResponse = mapper.readValue(response.getBody(), RefreshTokenResponse.class);
            oAuthClientRepo.deleteById(authorizedClient.getPrincipalName());

            final OAuth2AccessToken accessToken = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    refreshTokenResponse.getAccess_token(),
                    Instant.ofEpochSecond(refreshTokenResponse.getExpires_in()),
                    Instant.ofEpochSecond(refreshTokenResponse.getExpires_at())
            );

            final OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(
                    refreshTokenResponse.getRefresh_token(),
                    Instant.ofEpochSecond(refreshTokenResponse.getExpires_in())
            );

            final OAuth2AuthorizedClient newClient = new OAuth2AuthorizedClient(
                    authorizedClient.getClientRegistration(),
                    authorizedClient.getPrincipalName(),
                    accessToken,
                    refreshToken
            );

            final OAuthorizedClient OAuthorizedClient = new OAuthorizedClient();
            OAuthorizedClient.setPrincipalName(authorizedClient.getPrincipalName());
            OAuthorizedClient.setBytes(com.velokofi.hungryvelos.model.OAuthorizedClient.toBytes(newClient));
            oAuthClientRepo.save(OAuthorizedClient);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private URI getUri(final int pageNumber) throws URISyntaxException {
        final StringBuilder builder = new StringBuilder();
        builder.append("https://www.strava.com/api/v3/athlete/activities");
        builder.append("?per_page=200");
        builder.append("&after=").append("1609631999"); // Start of 3 Jan 2021
        builder.append("&page=").append(pageNumber);

        return new URI(builder.toString());
    }

    private String getTokenValue(final String clientId) {
        final OAuthorizedClient client = oAuthClientRepo.findById(clientId).get();
        final OAuth2AuthorizedClient entry = OAuthorizedClient.fromBytes(client.getBytes());
        final String tokenValue = entry.getAccessToken().getTokenValue();
        return tokenValue;
    }

}
