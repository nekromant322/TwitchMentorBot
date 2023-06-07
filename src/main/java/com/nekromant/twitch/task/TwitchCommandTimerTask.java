package com.nekromant.twitch.task;

import com.github.twitch4j.TwitchClient;
import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.service.TwitchAuthService;

import java.util.Collections;


public class TwitchCommandTimerTask implements Runnable {
    private final TwitchClient twitchClient;
    private final String channelName;
    private final TwitchCommand twitchCommand;
    private final TwitchAuthService twitchAuthService;

    public TwitchCommandTimerTask(TwitchClient twitchClient, String channelName, TwitchCommand twitchCommand, TwitchAuthService twitchAuthService) {
        this.twitchClient = twitchClient;
        this.channelName = channelName;
        this.twitchCommand = twitchCommand;
        this.twitchAuthService = twitchAuthService;
    }

    @Override
    public void run() {
        if (isLiveStream()) {
            twitchClient.getChat().sendMessage(channelName, twitchCommand.getResponse());
        }
    }

    private boolean isLiveStream() {
        return !twitchClient.getHelix().getStreams(twitchAuthService.getAuthToken().getAccessToken(), null,
                        null, 1, null, null, null, Collections.singletonList(channelName))
                .execute().getStreams().isEmpty();
    }
}
