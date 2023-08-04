package com.nekromant.twitch.service;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.Stream;
import com.nekromant.twitch.TwitchChatBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class TwitchLiveService {

    @Autowired
    private TwitchChatBot twitchChatBot;

    @Autowired
    private TwitchAuthService twitchAuthService;

    @Value("${twitch.channelName}")
    private String channelName;

    private TwitchClient twitchClient;

    @PostConstruct
    public void initTwitchClient() {
        twitchChatBot.validateConnection();
        twitchClient = twitchChatBot.getTwitchClient();
    }

    public boolean isLiveStream() {
        List<Stream> streams;
        try {
            streams = getStreams(twitchClient);
        } catch (Exception e) {
            log.error(e.getMessage());
            initTwitchClient();
            streams = getStreams(twitchClient);
        }
        return !streams.isEmpty();
    }

    private List<Stream> getStreams(TwitchClient twitchClient) {
        return twitchClient.getHelix().getStreams(twitchAuthService.getAuthToken()
                                .getAccessToken(), null, null, 1, null, null, null,
                        Collections.singletonList(channelName))
                .execute().getStreams();
    }
}
