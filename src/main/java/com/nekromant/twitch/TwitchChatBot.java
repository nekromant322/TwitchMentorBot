package com.nekromant.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import com.github.twitch4j.events.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.nekromant.twitch.model.TwitchToken;
import com.nekromant.twitch.service.ChannelPointsRedemptionService;
import com.nekromant.twitch.service.ResponseService;
import com.nekromant.twitch.service.TwitchAuthService;
import com.nekromant.twitch.service.TwitchCommandTimerService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
@Getter
@Component
public class TwitchChatBot {
    private TwitchCommandTimerService twitchCommandTimerService;
    private final static String PREFIX = "!";
    private TwitchClient twitchClient;
    private String channelName;
    private TwitchAuthService twitchAuthService;
    private ModerationTwitchHelix moderationTwitchHelix;
    private ChannelPointsRedemptionService channelPointsRedemptionService;
    private ResponseService responseService;

    @Autowired
    public TwitchChatBot(TwitchAuthService twitchAuthService,
                         @Value("${twitch.channelName}") String channelName,
                         ModerationTwitchHelix moderationTwitchHelix,
                         ChannelPointsRedemptionService channelPointsRedemptionService,
                         ResponseService responseService, TwitchCommandTimerService twitchCommandTimerService) {
        this.twitchAuthService = twitchAuthService;
        this.channelName = channelName;
        this.moderationTwitchHelix = moderationTwitchHelix;
        this.channelPointsRedemptionService = channelPointsRedemptionService;
        this.responseService = responseService;
        this.twitchCommandTimerService = twitchCommandTimerService;
        start();
    }

    public void onChatMessageEvent(ChannelMessageEvent event) {
        String message = event.getMessage();

        if (isCommand(message)) {
            String command = message.split(" ")[0].substring(1);

            responseService.response(event, command);
        }
    }

    public boolean isCommand(String message) {
        return message.startsWith(PREFIX);
    }

    public void start() {
        TwitchToken authToken = twitchAuthService.getAuthToken();
        String accessToken = twitchAuthService.validateToken(authToken) ?
                authToken.getAccessToken() :
                twitchAuthService.getAndSaveNewAuthTokenByRefreshToken(authToken.getRefreshToken()).getAccessToken();

        OAuth2Credential credential = new OAuth2Credential("twitch", accessToken);
        twitchClient = TwitchClientBuilder.builder()
                .withEnableHelix(true)
                .withEnableChat(true)
                .withEnablePubSub(true)
                .withChatAccount(credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();

        twitchClient.getChat().joinChannel(channelName);
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, this::onChatMessageEvent);

        TwitchToken moderationToken = twitchAuthService.getAndSaveNewModerationToken();
        moderationTwitchHelix.setHelix(twitchClient.getHelix());
        moderationTwitchHelix.setModerationToken(moderationToken.getAccessToken());

        OAuth2Credential credentialForChannelPoints = new OAuth2Credential("twitch", moderationToken.getAccessToken());
        String channelId = twitchClient.getChat().getChannelNameToChannelId().get(channelName);
        twitchClient.getPubSub().listenForChannelPointsRedemptionEvents(credentialForChannelPoints, channelId);
        twitchClient.getEventManager().onEvent(RewardRedeemedEvent.class, event -> channelPointsRedemptionService.onEvent(event));
    }

    public void restart() {
        if (twitchClient != null) {
            twitchClient.getChat().leaveChannel(channelName);
        }
        start();
    }

    @Scheduled(initialDelayString = "PT01H", fixedDelayString = "PT01H")
    public void validateConnection() {
        TwitchToken authToken = twitchAuthService.getAuthToken();
        if (!twitchAuthService.validateToken(authToken)) {
            twitchAuthService.getAndSaveNewAuthTokenByRefreshToken(authToken.getRefreshToken());
            restart();
        }
    }
    @PostConstruct
    public void startingCommandsByTime() {
        twitchCommandTimerService.setTwitchClient(twitchClient);
        twitchCommandTimerService.executedCommandsByTime();
    }
}
