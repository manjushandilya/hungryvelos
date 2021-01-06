package com.velokofi.hungryvelos;

import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ClubController {

    public static final String MEMBERS_URL = "https://www.strava.com/api/v3/clubs/803095/members";

    private RestTemplate restTemplate;

    public ClubController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/athlete")
    public String athlete(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        String url = "https://www.strava.com/api/v3/athlete";
        String tokenValue = client.getAccessToken().getTokenValue();
        return getResponse(tokenValue, url);
    }

    @GetMapping("/activities")
    public String activities(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        String url = "https://www.strava.com/api/v3/athlete/activities";
        String tokenValue = client.getAccessToken().getTokenValue();
        return getResponse(tokenValue, url);
    }

    @GetMapping("/clubs")
    public String clubs(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        String url = "https://www.strava.com/api/v3/athlete/clubs";
        String tokenValue = client.getAccessToken().getTokenValue();
        return getResponse(tokenValue, url);
    }

    @GetMapping("/clubMembers")
    public String clubMembers(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        String tokenValue = client.getAccessToken().getTokenValue();
        return getResponse(tokenValue, MEMBERS_URL);
    }

    @GetMapping("/clubActivities")
    public String clubActivities(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
                                 @RequestParam(required = false, defaultValue = "200") String per_page) {
        String url = "https://www.strava.com/api/v3/athlete/activities?per_page=" + per_page;
        String tokenValue = client.getAccessToken().getTokenValue();
        return getResponse(tokenValue, url);
    }

    private String getResponse(String tokenValue, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + tokenValue);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        return response.getBody();
    }

}
