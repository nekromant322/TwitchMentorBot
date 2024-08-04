package com.nekromant.twitch.service;

import com.github.twitch4j.TwitchClient;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.nekromant.twitch.TwitchChatBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class YouTubeService {
    private final static String WATCH = "https://www.youtube.com/watch?v=";
    private final TwitchLiveService twitchLiveService;
    private final TwitchChatBot twitchChatBot;
    private final YouTubeAuth youTubeAuth;
    @Value("${twitch.channelName}")
    private String twitchChannelName;
    @Value("${youtube.channel.id}")
    private String youtubeChannelId;
    @Value("${youtube.apiKey}")
    private String apiKey;

    public void sendInfoAboutLastVideo() {
        if (twitchLiveService.isLiveStream()) {
            YouTube youtubeClient = youTubeAuth.getYoutubeClient();

            SearchListResponse response = getLastVideoInfo(youtubeClient);

            if (response != null) {
                String message = buildAdMessageForTwitch(response);
                TwitchClient twitchClient = twitchChatBot.getTwitchClient();
                twitchClient.getChat().sendMessage(twitchChannelName, message);
                log.info("Running task for sending info about last video");
            } else {
                System.out.println("Response is null");
            }
        }
    }

    private static String buildAdMessageForTwitch(SearchListResponse response) {
        SearchResult searchResult = response.getItems().get(0);

        SearchResultSnippet snippet = searchResult.getSnippet();
        ResourceId videoId = searchResult.getId();
        String title = snippet.getTitle();
        String videoLink = WATCH + videoId.getVideoId();

        return "Зацени моё последнее видео! \"" + title + "\": " + videoLink;
    }

    private SearchListResponse getLastVideoInfo(YouTube youtubeClient) {
        YouTube.Search.List videos = null;
        try {
            videos = youtubeClient.search().list(List.of("snippet"));
        } catch (IOException e) {
            log.error("Error while setting up request for searching youtube video. {}", e.getMessage(), e);
        }
        SearchListResponse response = null;
        if (videos != null) {
            videos.setKey(apiKey);
            videos.setMaxResults(1L);
            videos.setOrder("date");
            videos.setChannelId(youtubeChannelId);
            try {
                response = videos.execute();
            } catch (IOException e) {
                log.error("Error while executing search of youtube video. {}", e.getMessage(), e);
            }
        }
        return response;
    }
}