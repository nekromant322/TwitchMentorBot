package com.nekromant.twitch.service.reward;

import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.nekromant.twitch.TwitchChatBot;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.service.DailyBonusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmothieRewardService implements RewardService {

    @Autowired
    private DailyBonusService dailyBonusService;

    @Lazy
    @Autowired
    private TwitchChatBot twitchChatBot;

    private final String BONUS_SUCCESS = " выпил смузи";
    private final String BONUS_FAILED = " уже пил смузи сегодня";

    @Override
    public void process(RewardRedeemedEvent event, String username) {
        Long userId = Long.valueOf(event.getRedemption().getUser().getId());
        String displayName = event.getRedemption().getUser().getDisplayName();
        if (dailyBonusService.takeBonus(userId, displayName)) {
            log.info(username + " " + BONUS_SUCCESS);
        } else {
            log.info(username + " " + BONUS_FAILED);
        }
    }

    @Override
    public String getRewardTitle() {
        return "Выпить смузи";
    }
}
