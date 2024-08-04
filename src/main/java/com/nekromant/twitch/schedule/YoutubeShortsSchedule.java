package com.nekromant.twitch.schedule;

import com.nekromant.twitch.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class YoutubeShortsSchedule {
    private final YouTubeService youtubeService;

    @Scheduled(fixedDelayString = "#{@scheduleConfigProperties.periodSendYoutubeShorts}")
    public void sendYoutubeShorts() {
        youtubeService.sendInfoAboutLastVideo();
    }
}