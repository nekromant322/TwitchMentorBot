package com.nekromant.twitch.schedule;

import com.nekromant.twitch.TwitchChatBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ConnectionSchedule {
    @Autowired
    private TwitchChatBot twitchChatBot;

    @Scheduled(initialDelayString = "#{@scheduleConfigProperties.periodValidateConnection}",
            fixedDelayString = "#{@scheduleConfigProperties.periodValidateConnection}")
    public void validateConnection() {
        twitchChatBot.validateConnection();
    }
}
