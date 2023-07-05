package com.nekromant.twitch.config;

import com.nekromant.twitch.model.TwitchCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashSet;

@Configuration
public class LinkedHashSetOfTwitchCommandConfig {

    @Bean
    public LinkedHashSet<TwitchCommand> getTwitchCommandLinkedHashSet() {
        return new LinkedHashSet<>();
    }
}
