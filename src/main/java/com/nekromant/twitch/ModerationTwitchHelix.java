package com.nekromant.twitch;

import com.github.twitch4j.helix.TwitchHelix;
import com.github.twitch4j.helix.domain.BanUserInput;
import org.springframework.stereotype.Component;

@Component
public class ModerationTwitchHelix {
    private TwitchHelix helix;
    private String moderationToken;

    public void setHelix(TwitchHelix helix) {
        this.helix = helix;
    }

    public void setModerationToken(String moderationToken) {
        this.moderationToken = moderationToken;
    }

    public TwitchHelix getHelix() {
        return helix;
    }

    public String getModerationToken() {
        return moderationToken;
    }

    public void banUser(String authToken, String broadcasterId, String moderatorId, BanUserInput data) {
        this.helix.banUser(authToken, broadcasterId, moderatorId, data);
    }
}