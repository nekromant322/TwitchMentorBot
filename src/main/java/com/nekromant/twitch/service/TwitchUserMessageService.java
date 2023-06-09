package com.nekromant.twitch.service;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.model.TwitchUserMessage;
import com.nekromant.twitch.repository.TwitchUserMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TwitchUserMessageService {
    @Autowired
    private TwitchUserMessageRepository twitchUserMessageRepository;

    public List<TwitchUserMessage> getTwitchUserMessageByName(String name) {
        return twitchUserMessageRepository.getTwitchUserMessageByName(name);
    }

    public void saveTwitchUserMessage(ChannelMessageEvent event) {
        TwitchUserMessage twitchUserMessage = new TwitchUserMessage(event.getMessageEvent().getUser().getName(), event.getMessage());
        twitchUserMessageRepository.save(twitchUserMessage);

    }
}
