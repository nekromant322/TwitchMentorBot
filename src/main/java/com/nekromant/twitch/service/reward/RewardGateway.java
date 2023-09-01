package com.nekromant.twitch.service.reward;

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardGateway {
    private Map<String, RewardService> strategies = new HashMap<>();

    @Autowired
    public RewardGateway(List<RewardService> rewardServices) {
        for (RewardService rewardService : rewardServices) {
            strategies.put(rewardService.getRewardTitle(), rewardService);
        }
    }

    public void processEvent(RewardRedeemedEvent event) {
        String username = event.getRedemption().getUser().getLogin();
        strategies.get(event.getRedemption().getReward().getTitle()).process(event, username);
    }
}
