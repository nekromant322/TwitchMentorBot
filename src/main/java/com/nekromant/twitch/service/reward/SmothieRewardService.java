package com.nekromant.twitch.service.reward;

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.nekromant.twitch.service.DailyBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmothieRewardService implements RewardService {

    @Autowired
    private DailyBonusService dailyBonusService;

    @Override
    public void process(RewardRedeemedEvent event, String username) {
        Long userId = Long.valueOf(event.getRedemption().getUser().getId());
        String displayName = event.getRedemption().getUser().getDisplayName();
        dailyBonusService.takeBonus(userId, displayName);
    }

    @Override
    public String getRewardTitle() {
        return "Выпить смузи";
    }
}
