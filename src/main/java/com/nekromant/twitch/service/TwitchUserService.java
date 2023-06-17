package com.nekromant.twitch.service;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.model.TwitchUserMessage;
import com.nekromant.twitch.repository.TwitchUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TwitchUserService {
    @Autowired
    private TwitchUserRepository twitchUserRepository;

    public void saveTwitchUserMessage(ChannelMessageEvent event) {
        Long idTwitchUser = Long.valueOf(event.getMessageEvent().getUser().getId());
        String message = event.getMessage();
        if (message.startsWith("!")) {
            return;
        }
        String nameTwitchUser = event.getMessageEvent().getUser().getName();
        TwitchUser twitchUser = getTwitchUserById(idTwitchUser);
        if (twitchUser != null) {
            addMessage(twitchUser, message);

        } else {
            TwitchUser newTwitchUser = new TwitchUser(idTwitchUser, nameTwitchUser);
            addMessage(newTwitchUser, message);
        }

    }

    public TwitchUser getTwitchUserById(Long id) {
        Optional<TwitchUser> twitchUser = twitchUserRepository.findById(id);
        return twitchUser.orElse(null);

    }

    private void addMessage(TwitchUser twitchUser, String message) {
        TwitchUserMessage twitchUserMessage = new TwitchUserMessage(message);
        twitchUser.getMessages().add(twitchUserMessage);
        twitchUserMessage.setTwitchUser(twitchUser);
        save(twitchUser);
    }

    public void save(TwitchUser twitchUser) {
        twitchUserRepository.save(twitchUser);
    }

    public TwitchUser getTwitchUserWithMostMessages() {
        return twitchUserRepository.findFirst();
    }
}
