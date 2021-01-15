package com.velokofi.hungryvelos.cron;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.velokofi.hungryvelos.model.AthleteActivity;
import com.velokofi.hungryvelos.model.AuthorizedClient;
import com.velokofi.hungryvelos.model.RefreshTokenRequest;
import com.velokofi.hungryvelos.model.RefreshTokenResponse;
import com.velokofi.hungryvelos.persistence.AthleteActivityRepository;
import com.velokofi.hungryvelos.persistence.AuthorizedClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ActivityUpdater {

    @Autowired
    private AthleteActivityRepository athleteActivityRepo;

    @Autowired
    private AuthorizedClientRepository oAuthClientRepo;

    @Scheduled(fixedRate = 60 * 1000 * 15, initialDelay = 60 * 1000 * 5)
    public void run() {
        System.out.println("Running scheduled task at: " + LocalDateTime.now());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        oAuthClientRepo.findAll().stream().filter(
                e -> AuthorizedClient.fromBytes(e.getBytes()).getAccessToken().getExpiresAt().isBefore(Instant.now())
        ).forEach(e -> oAuthClientRepo.save(e));

        for (final AuthorizedClient client : oAuthClientRepo.findAll()) {
            try {
                final OAuth2AuthorizedClient entry = AuthorizedClient.fromBytes(client.getBytes());
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
                    if (activitiesResponse.getStatusCode().is4xxClientError()) {
                        System.out.println("Request failed for client: " + client.getPrincipalName() + " with status code: " + activitiesResponse.getStatusCode());
                        System.out.println("Expires at is: " + entry.getAccessToken().getExpiresAt() + ", Instant.now() is: " + Instant.now());
                        refresh(client);
                    } else if (activitiesResponse.getStatusCode().is2xxSuccessful()) {
                        AthleteActivity[] activitiesArray = mapper.readValue(activitiesResponse.getBody(), AthleteActivity[].class);
                        if (activitiesArray.length < 1) {
                            break;
                        }
                        Stream.of(activitiesArray).forEach(activity -> athleteActivityRepo.save(activity));
                    } else {
                        break;
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void refresh(final AuthorizedClient entry) {
        final OAuth2AuthorizedClient client = AuthorizedClient.fromBytes(entry.getBytes());
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

            final RefreshTokenResponse refreshTokenResponse = mapper.readValue(response.getBody(), RefreshTokenResponse.class);
            oAuthClientRepo.deleteById(client.getPrincipalName());

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

            final AuthorizedClient AuthorizedClient = new AuthorizedClient();
            AuthorizedClient.setPrincipalName(client.getPrincipalName());
            AuthorizedClient.setBytes(AuthorizedClient.toBytes(newClient));
            oAuthClientRepo.save(AuthorizedClient);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
