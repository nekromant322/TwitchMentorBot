package com.nekromant.twitch.service;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.model.TwitchUserMessage;
import com.nekromant.twitch.repository.TwitchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TwitchUserService {
    @Autowired
    private TwitchUserRepository twitchUserRepository;

    public void saveTwitchUserMessage(ChannelMessageEvent event) {
        Long idTwitchUser = Long.valueOf(event.getMessageEvent().getUser().getId());
        String message = event.getMessage();
        String nameTwitchUser = event.getMessageEvent().getUser().getName();
        Optional<TwitchUser> twitchUser = twitchUserRepository.findById(idTwitchUser);
        if (twitchUser.isPresent()) {
            save(twitchUser.get(), message);

        } else {
            TwitchUser newTwitchUser = new TwitchUser(idTwitchUser, nameTwitchUser);
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
