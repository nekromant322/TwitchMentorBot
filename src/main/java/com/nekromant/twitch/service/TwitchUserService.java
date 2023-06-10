package com.nekromant.twitch.service;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.model.TwitchUserMessage;
import com.nekromant.twitch.repository.TwitchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TwitchUserService {
    @Autowired
    private TwitchUserRepository twitchUserRepository;

    public void saveTwitchUserMessage(ChannelMessageEvent event) {
        String message = event.getMessage();
        String nameTwitchUser = event.getMessageEvent().getUser().getName();
        TwitchUser twitchUser = twitchUserRepository.findByName(nameTwitchUser);
        if (twitchUser != null) {
            save(twitchUser, message);

        } else {
            TwitchUser newTwitchUser = new TwitchUser(nameTwitchUser);
            save(newTwitchUser, message);
        }
    }

    private void save(TwitchUser twitchUser, String message) {
        TwitchUserMessage twitchUserMessage = new TwitchUserMessage(message);
        twitchUser.getMessages().add(twitchUserMessage);
        twitchUserMessage.setTwitchUser(twitchUser);
        twitchUserRepository.save(twitchUser);

    }
}
