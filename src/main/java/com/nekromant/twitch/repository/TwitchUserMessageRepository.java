package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchUserMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TwitchUserMessageRepository extends CrudRepository<TwitchUserMessage, Long> {
    List<TwitchUserMessage> getTwitchUserMessageByName(String name);
}
