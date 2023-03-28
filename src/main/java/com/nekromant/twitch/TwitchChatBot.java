package com.nekromant.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.command.BotCommand;
import com.nekromant.twitch.model.TwitchToken;
import com.nekromant.twitch.service.TwitchAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class TwitchChatBot {
    private final static String PREFIX = "!";

    private HashMap<String, BotCommand> botCommands;
    private TwitchClient twitchClient;
    private String channelName;
    private TwitchAuthService twitchAuthService;
    private ModerationTwitchHelix moderationTwitchHelix;

    @Autowired
    public TwitchChatBot(TwitchAuthService twitchAuthService, List<BotCommand> allCommands, @Value("${twitch.channelName}") String channelName, ModerationTwitchHelix moderationTwitchHelix) {
        this.twitchAuthService = twitchAuthService;
        this.channelName = channelName;
        this.moderationTwitchHelix = moderationTwitchHelix;
        start();
        botCommands = new HashMap<>();
        allCommands.forEach(command -> botCommands.put(command.getCommandIdentifier(), command));
    }

    public void onChatMessageEvent(ChannelMessageEvent event) {
        String message = event.getMessage();

        if (isCommand(message)) {
            String command = message.split(" ")[0].substring(1);

            if (botCommands.containsKey(command)) {
                botCommands.get(command).processMessage(event);
            }
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
                .withChatAccount(credential)
                .withDefaultEventHandler(SimpleEventHandler.class)
                .build();

        twitchClient.getChat().joinChannel(channelName);
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> onChatMessageEvent(event));

        TwitchToken moderationToken = twitchAuthService.getAndSaveNewModerationToken();
        moderationTwitchHelix.setHelix(twitchClient.getHelix());
        moderationTwitchHelix.setModerationToken(moderationToken.getAccessToken());
    }

    public void restart() {
        if (twitchClient != null) {
            twitchClient.getChat().leaveChannel(channelName);
        }
        start();
    }

    @Scheduled(initialDelayString = "PT01H" , fixedDelayString = "PT01H")
    public void validateConnection() {
        TwitchToken authToken = twitchAuthService.getAuthToken();

        if (!twitchAuthService.validateToken(authToken)) {
            twitchAuthService.getAndSaveNewAuthTokenByRefreshToken(authToken.getRefreshToken());
            restart();
        }
    }
}
