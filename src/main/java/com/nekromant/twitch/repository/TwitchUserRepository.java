package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TwitchUserRepository extends JpaRepository<TwitchUser, Long> {
    @Query(value = "SELECT u FROM twitch_users u WHERE size(u.messages) " +
            "= (SELECT MAX(size(u2.messages)) FROM twitch_users u2)")
    TwitchUser findTwitchUserWithMaxMessages();
}
