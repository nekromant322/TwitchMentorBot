package com.nekromant.twitch.task;

import com.nekromant.twitch.TwitchClientHolder;
import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.service.TwitchAuthService;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class TwitchCommandTimerTask implements Runnable {
    private final TwitchClientHolder twitchClientHolder;
    private final String channelName;
    private final TwitchCommand twitchCommand;
    private final TwitchAuthService twitchAuthService;

    public TwitchCommandTimerTask(TwitchClientHolder twitchClientHolder, String channelName, TwitchCommand twitchCommand,
                                  TwitchAuthService twitchAuthService) {
        this.twitchClientHolder = twitchClientHolder;
        this.channelName = channelName;
        this.twitchCommand = twitchCommand;
        this.twitchAuthService = twitchAuthService;
    }

    @Override
    public void run() {
        if (isLiveStream()) {
            twitchClientHolder.getTwitchClient().getChat().sendMessage(channelName, twitchCommand.getResponse());
            log.info("Running task for command: !" + twitchCommand.getName());
        }
    }

    private boolean isLiveStream() {
        return !twitchClientHolder.getTwitchClient().getHelix().getStreams(twitchAuthService.getAuthToken()
                                .getAccessToken(), null, null, 1, null, null, null,
                        Collections.singletonList(channelName))
                .execute().getStreams().isEmpty();
    }
}
