package com.nekromant.twitch.service.reward;

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;

public interface RewardService {
    void process(RewardRedeemedEvent event, String username);
    String getRewardTitle();
}
