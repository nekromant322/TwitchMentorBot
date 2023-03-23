package com.nekromant.twitch.config;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.nekromant.twitch.service.TwitchAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitchConfig {
    @Value("${twitch.channelName}")
    private String channelName;

    @Autowired
    private TwitchAuthService twitchAuthService;

    @Bean
    public TwitchClient getTwitchClient() {
        String authToken = twitchAuthService.getAuthToken();

        OAuth2Credential credential = new OAuth2Credential("twitch", authToken);
        TwitchClient twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withChatAccount(credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();

        twitchClient.getChat().joinChannel(channelName);

        return twitchClient;
    }
}
