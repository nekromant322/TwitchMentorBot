package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TwitchUserRepository extends JpaRepository<TwitchUser, Long> {
    @Query(value = "SELECT u.* FROM twitch_users u JOIN twitch_message m ON u.id = m.twitch_user_id GROUP BY u.id " +
            "ORDER BY COUNT(m.id) DESC LIMIT 1", nativeQuery = true)
    TwitchUser findFirst();
}
