package com.spendsmart.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spendsmart.auth.dto.GoogleTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class GoogleOAuthService {

    private static final String GOOGLE_TOKEN_ENDPOINT = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USERINFO_ENDPOINT = "https://openidconnect.googleapis.com/v1/userinfo";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final GoogleOAuthProperties properties;

    public GoogleOAuthService(RestTemplate restTemplate,
                              ObjectMapper objectMapper,
                              GoogleOAuthProperties properties) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.properties = properties;
    }

    /**
     * Exchange Google authorization code for access token
     */
    public GoogleTokenResponse exchangeCodeForToken(String code, String codeVerifier, String redirectUri) {
        try {
            log.info("Exchanging Google OAuth code for token");

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("client_id", properties.getGoogle().getClientId());
            requestBody.add("client_secret", properties.getGoogle().getClientSecret());
            requestBody.add("code", code);
            requestBody.add("grant_type", "authorization_code");
            requestBody.add("redirect_uri", redirectUri);
            requestBody.add("code_verifier", codeVerifier);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            // Exchange code for token
            ResponseEntity<String> tokenResponse = restTemplate.postForEntity(
                GOOGLE_TOKEN_ENDPOINT,
                requestEntity,
                String.class
            );
            String responseStr = tokenResponse.getBody();
            if (responseStr == null || responseStr.isBlank()) {
                throw new RuntimeException("Empty token response from Google");
            }

            JsonNode jsonNode = objectMapper.readTree(responseStr);
            if (!jsonNode.hasNonNull("access_token")) {
                throw new RuntimeException("Google token response did not include access_token");
            }
            
            GoogleTokenResponse response = new GoogleTokenResponse();
            response.setAccessToken(jsonNode.get("access_token").asText());
            
            if (jsonNode.has("id_token")) {
                response.setIdToken(jsonNode.get("id_token").asText());
            }
            
            response.setTokenType(jsonNode.get("token_type").asText());
            response.setExpiresIn(jsonNode.get("expires_in").asLong());

            // Get user info from Google
            GoogleUserInfo userInfo = getUserInfo(response.getAccessToken());
            if (userInfo.getVerifiedEmail() != null && !userInfo.getVerifiedEmail()) {
                throw new RuntimeException("Google account email is not verified");
            }
            response.setEmail(userInfo.getEmail());
            response.setFullName(userInfo.getName());
            response.setPicture(userInfo.getPicture());

            log.info("Successfully exchanged Google OAuth code for token, user: {}", userInfo.getEmail());
            return response;

        } catch (Exception e) {
            log.error("Failed to exchange Google OAuth code", e);
            throw new RuntimeException("Failed to exchange Google OAuth code: " + e.getMessage());
        }
    }

    /**
     * Decode ID token to get user claims
     */
    public Map<String, Object> decodeIdToken(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid token format");
            }

            // Decode payload (second part)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            return objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            log.error("Failed to decode ID token", e);
            throw new RuntimeException("Failed to decode ID token");
        }
    }

    /**
     * Get user info from Google using access token
     */
    private GoogleUserInfo getUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                GOOGLE_USERINFO_ENDPOINT,
                org.springframework.http.HttpMethod.GET,
                requestEntity,
                String.class
            );

            String responseStr = response.getBody();
            if (responseStr == null || responseStr.isBlank()) {
                throw new RuntimeException("Empty user info response from Google");
            }

            return objectMapper.readValue(responseStr, GoogleUserInfo.class);
        } catch (Exception e) {
            log.error("Failed to get user info from Google", e);
            throw new RuntimeException("Failed to get user info from Google: " + e.getMessage());
        }
    }

    public String getClientId() {
        return properties.getGoogle().getClientId();
    }

    /**
     * Inner class for Google user info response
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class GoogleUserInfo {
        public String sub;
        public String id;
        public String email;
        public String name;
        public String picture;
        @JsonProperty("verified_email")
        public Boolean verifiedEmailLegacy;
        @JsonProperty("email_verified")
        public Boolean emailVerified;

        public String getSub() { return sub; }
        public String getId() { return id; }
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getPicture() { return picture; }
        public Boolean getVerifiedEmail() {
            return emailVerified != null ? emailVerified : verifiedEmailLegacy;
        }
    }
}
