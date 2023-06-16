package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.model.TwitchUserMessage;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface TwitchUserMessageRepository extends CrudRepository<TwitchUserMessage, Long> {
    @Transactional
    void deleteAllByTwitchUser(TwitchUser twitchUser);
}
