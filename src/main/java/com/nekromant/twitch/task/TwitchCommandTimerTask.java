package com.nekromant.twitch.task;

import com.github.twitch4j.TwitchClient;
import com.nekromant.twitch.model.TwitchCommand;
import org.springframework.stereotype.Component;

@Component
public class TwitchCommandTimerTask implements Runnable {
    private TwitchClient twitchClient;
    private String channelName;
    private TwitchCommand twitchCommand;

    public TwitchCommandTimerTask() {
    }

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
