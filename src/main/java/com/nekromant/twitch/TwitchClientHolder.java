package com.nekromant.twitch;

import com.github.twitch4j.TwitchClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class TwitchClientHolder {
    private TwitchClient twitchClient;
}
