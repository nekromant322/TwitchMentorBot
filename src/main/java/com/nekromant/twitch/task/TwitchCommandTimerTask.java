package com.nekromant.twitch.task;

import com.github.twitch4j.TwitchClient;
import com.nekromant.twitch.model.TwitchCommand;


public class TwitchCommandTimerTask implements Runnable {
    private final TwitchClient twitchClient;
    private final String channelName;
    private final TwitchCommand twitchCommand;

    public TwitchCommandTimerTask(TwitchClient twitchClient, String channelName, TwitchCommand twitchCommand) {
        this.twitchClient = twitchClient;
        this.channelName = channelName;
        this.twitchCommand = twitchCommand;
    }

    @Override
    public void run() {
        twitchClient.getChat().sendMessage(channelName, twitchCommand.getResponse());
    }
}
