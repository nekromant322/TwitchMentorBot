package com.nekromant.twitch.service;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.nekromant.twitch.model.RedeemedPixels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChannelPointsRedemptionService {
    private final static String ONE_PIXEL_REWARD_TITLE = "pixel(не работает)";
    private final static String FIVE_PIXELS_REWARD_TITLE = "5 пикселей(не работает)";
    private final static String TWENTY_FIVE_PIXELS_REWARD_TITLE = "25 пикселей(не работает)";
    @Value("${pixelWars.width}")
    private int width;
    @Value("${pixelWars.height}")
    private int height;
    @Value("${twitch.channelName}")
    private String channelName;

    @Autowired
    private RedeemedPixelsService redeemedPixelsService;

    public void onEvent(RewardRedeemedEvent event) {
        ChannelPointsRedemption redemption = event.getRedemption();
        ChannelPointsReward reward = redemption.getReward();
        String username = redemption.getUser().getLogin();
        int pixelCount = 0;

        switch (reward.getTitle()) {
            case ONE_PIXEL_REWARD_TITLE:
                pixelCount = 1;
                break;
            case FIVE_PIXELS_REWARD_TITLE:
                pixelCount = 5;
                break;
            case TWENTY_FIVE_PIXELS_REWARD_TITLE:
                pixelCount = 25;
                break;
        }

        savePixel(username, pixelCount);
    }

    public RedeemedPixels savePixel(String username, int count) {
        return redeemedPixelsService.save(username, count);
    }
}
