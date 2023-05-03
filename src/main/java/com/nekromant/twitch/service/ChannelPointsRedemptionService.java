package com.nekromant.twitch.service;

import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChannelPointsRedemptionService {
    @Value("${twitch.rewardTitle.onePixelReward}")
    private String ONE_PIXEL_REWARD_TITLE;
    @Value("${twitch.rewardTitle.fivePixelReward}")
    private String FIVE_PIXELS_REWARD_TITLE;
    @Value("${twitch.rewardTitle.twentyFivePixelReward}")
    private String TWENTY_FIVE_PIXELS_REWARD_TITLE;

    @Autowired
    private RedeemedPixelsService redeemedPixelsService;

    public void onEvent(RewardRedeemedEvent event) {
        ChannelPointsRedemption redemption = event.getRedemption();
        ChannelPointsReward reward = redemption.getReward();
        String username = redemption.getUser().getLogin();
        String rewardTitle = reward.getTitle();
        int pixelCount = 0;

        if (rewardTitle.equals(ONE_PIXEL_REWARD_TITLE)) {
            pixelCount = 1;
        } else if (rewardTitle.equals(FIVE_PIXELS_REWARD_TITLE)) {
            pixelCount = 5;
        } else if (rewardTitle.equals(TWENTY_FIVE_PIXELS_REWARD_TITLE)) {
            pixelCount = 25;
        }

        savePixel(username, pixelCount);
    }

    public void savePixel(String username, int count) {
        redeemedPixelsService.save(username, count);
    }
}
