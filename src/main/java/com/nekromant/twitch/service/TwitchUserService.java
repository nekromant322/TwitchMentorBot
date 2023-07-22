package com.nekromant.twitch.service;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import com.github.twitch4j.helix.domain.BanUserInput;
import com.nekromant.twitch.ModerationTwitchHelix;
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
    @Autowired
    private ModerationTwitchHelix moderationTwitchHelix;
    private final String USER_NEEDS_A_TIMEOUT = "meowhardy";
    private int timeoutUserMessageCount = 0;

    public void saveTwitchUserMessage(ChannelMessageEvent event) {
        Long idTwitchUser = Long.valueOf(event.getMessageEvent().getUser().getId());
        String message = event.getMessage().replaceAll("\\d", "");
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

    public TwitchUser getTwitchUserByName(String name) {
        return twitchUserRepository.findTwitchUserByName(name);
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

    public void tryToTimeoutUser(ChannelMessageEvent event, String authToken, String broadcasterId, String moderatorId) {
        System.out.println("trying to timeout");
        EventUser eventUser = event.getUser();

        if (eventUser.getName().equals(USER_NEEDS_A_TIMEOUT)) {
            this.timeoutUserMessageCount++;
            System.out.println("Сообщений: " + timeoutUserMessageCount);

            if (this.timeoutUserMessageCount == 2) {
                this.timeoutUserMessageCount = 0;
                BanUserInput banUserInput = new BanUserInput(USER_NEEDS_A_TIMEOUT, "REASON", 600);
                moderationTwitchHelix.banUser(authToken, broadcasterId, moderatorId, banUserInput);
                System.out.println("authToken: " + authToken);
                System.out.println("broadcasterId: " + broadcasterId);
                System.out.println("moderatorId: " + moderatorId);
                System.out.println(banUserInput.getUserId());
                System.out.println("banned");
//                как то кинуть таймаут
            }
        }
    }
}
