package com.nekromant.twitch.service;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Component
public class YouTubeAuth {
    public static final String SCOPE = "https://www.googleapis.com/auth/youtube.readonly";
    @Value("${youtube.auth.clientId}")
    private String clientId;
    @Value("${youtube.auth.secret}")
    private String clientSecret;
    private YouTube youtubeClient;

    public YouTube getYoutubeClient() {
        if (youtubeClient == null) {
            HttpTransport httpTransport;
            try {
                httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            } catch (GeneralSecurityException | IOException e) {
                log.error("Error while creating connection client for youtube. {}", e.getMessage(), e);
                throw new RuntimeException(e);
            }
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            AuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, jsonFactory, clientId, clientSecret, List.of(SCOPE)).build();
            HttpRequestInitializer requestInitializer = flow.getRequestInitializer();

            youtubeClient = new YouTube.Builder(httpTransport, jsonFactory, requestInitializer).setApplicationName("YoutubeService").build();
        }
        return youtubeClient;
    }
}
