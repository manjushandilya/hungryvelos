package com.velokofi.hungryvelos.cron;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.AthleteActivity;
import com.velokofi.hungryvelos.model.RefreshTokenRequest;
import com.velokofi.hungryvelos.model.RefreshTokenResponse;
import com.velokofi.hungryvelos.repository.PersistenceManager;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class ScheduledTasks {

    @Scheduled(fixedRate = 100000, initialDelay = 50000)
    public void run() {
        System.out.println("Running scheduled task at: " + LocalDateTime.now());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PersistenceManager.retrieveClients().stream().filter(
                e -> e.getAccessToken().getExpiresAt().isBefore(Instant.now())
        ).forEach(e -> refresh(e));

        for (final OAuth2AuthorizedClient entry : PersistenceManager.retrieveClients()) {
            try {
                final String tokenValue = entry.getAccessToken().getTokenValue();

                for (int page = 1; ; page++) {
                    final StringBuilder builder = new StringBuilder();
                    builder.append("https://www.strava.com/api/v3/athlete/activities");
                    builder.append("?per_page=200");
                    builder.append("&after=").append("1609631999"); // Start of 3 Jan 2021
                    builder.append("&page=").append(page);

                    URI uri = new URI(builder.toString());

                    final RestTemplate restTemplate = new RestTemplate();
                    final HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("Authorization", "Bearer " + tokenValue);
                    final HttpEntity<String> request = new HttpEntity<String>(headers);

                    final ResponseEntity<String> activitiesResponse = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);
                    if (activitiesResponse.getStatusCode().is2xxSuccessful()) {
                        AthleteActivity[] activitiesArray = mapper.readValue(activitiesResponse.getBody(), AthleteActivity[].class);
                        if (activitiesArray.length < 1) {
                            break;
                        }
                        Stream.of(activitiesArray).forEach(activity -> PersistenceManager.persistActivity(activity));
                    } else {
                        break;
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void refresh(final OAuth2AuthorizedClient client) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final StringBuilder builder = new StringBuilder();
            builder.append("https://www.strava.com/api/v3/oauth/token");

            URI uri = new URI(builder.toString());

            final RestTemplate restTemplate = new RestTemplate();
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            final RefreshTokenRequest requestObj = new RefreshTokenRequest();
            requestObj.setClient_id(client.getClientRegistration().getClientId());
            requestObj.setClient_secret(client.getClientRegistration().getClientSecret());
            requestObj.setGrant_type("refresh_token");
            requestObj.setRefresh_token(client.getRefreshToken().getTokenValue());
            final String body = mapper.writeValueAsString(requestObj);

            //System.out.println("Refresh token request: " + body);

            final HttpEntity<String> request = new HttpEntity<String>(body, headers);

            final ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
            //System.out.println("Refresh token response: " + response);

            RefreshTokenResponse refreshTokenResponse = mapper.readValue(response.getBody(), RefreshTokenResponse.class);
            PersistenceManager.deleteClient(client.getPrincipalName());

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
                    client.getClientRegistration(),
                    client.getPrincipalName(),
                    accessToken,
                    refreshToken
            );

            PersistenceManager.persistClient(newClient);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
