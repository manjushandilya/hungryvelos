package com.velokofi.hungryvelos;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.util.ArrayList;
import java.util.List;

public class TokenStore {

    public static final List<OAuth2AuthorizedClient> TOKENS = new ArrayList<>();

}
