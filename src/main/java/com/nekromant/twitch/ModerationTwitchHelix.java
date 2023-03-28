package com.nekromant.twitch;

import com.github.twitch4j.helix.TwitchHelix;
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
}
