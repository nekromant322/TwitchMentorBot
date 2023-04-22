package com.nekromant.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.nekromant.twitch.command.BotCommand;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.model.TwitchToken;
import com.nekromant.twitch.service.ChannelPointsRedemptionService;
import com.nekromant.twitch.service.TwitchAuthService;
import com.nekromant.twitch.service.TwitchCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class TwitchChatBot {
    private final static String PREFIX = "!";

    private HashMap<String, TwitchCommand> botCommandsDB;
    private HashMap<String, BotCommand> botCommandsClass;
    private TwitchClient twitchClient;
    private String channelName;
    private TwitchAuthService twitchAuthService;
    private ModerationTwitchHelix moderationTwitchHelix;
    private ChannelPointsRedemptionService channelPointsRedemptionService;
    private TwitchCommandService twitchCommandService;

    @Autowired
    public TwitchChatBot(TwitchAuthService twitchAuthService,
                         List<BotCommand> allCommandsClass,
                         @Value("${twitch.channelName}") String channelName,
                         ModerationTwitchHelix moderationTwitchHelix,
                         ChannelPointsRedemptionService channelPointsRedemptionService,
                         TwitchCommandService twitchCommandService) {
        this.twitchAuthService = twitchAuthService;
        this.channelName = channelName;
        this.moderationTwitchHelix = moderationTwitchHelix;
        this.channelPointsRedemptionService = channelPointsRedemptionService;
        start();
        this.twitchCommandService = twitchCommandService;
        botCommandsClass = new HashMap<>();
        allCommandsClass.forEach(command -> botCommandsClass.put(command.getCommandIdentifier(), command));
    }

    public void onChatMessageEvent(ChannelMessageEvent event) {
        List<TwitchCommand> allCommands = twitchCommandService.getCommands();
        botCommandsDB = new HashMap<>();
        allCommands.forEach(command -> botCommandsDB.put(command.getName(), command));

        String message = event.getMessage();

        if (isCommand(message)) {
            String command = message.split(" ")[0].substring(1);

            if (botCommandsDB.containsKey(command) && botCommandsDB.get(command).isEnabled()) {
                String channelName = event.getChannel().getName();
                String senderUsername = event.getMessageEvent().getUser().getName();
                Message replyMessage = new Message(senderUsername, botCommandsDB.get(command).getResponse());
                event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
            }

            if (botCommandsClass.containsKey(command)) {
                botCommandsClass.get(command).processMessage(event);
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

    @Scheduled(initialDelayString = "PT01H" , fixedDelayString = "PT01H")
    public void validateConnection() {
        TwitchToken authToken = twitchAuthService.getAuthToken();

        if (!twitchAuthService.validateToken(authToken)) {
            twitchAuthService.getAndSaveNewAuthTokenByRefreshToken(authToken.getRefreshToken());
            restart();
        }
    }
}
