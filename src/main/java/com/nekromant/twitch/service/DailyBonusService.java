package com.nekromant.twitch.service;

import com.github.twitch4j.common.events.domain.EventUser;
import com.nekromant.twitch.model.DailyBonus;
import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.repository.DailyBonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class DailyBonusService {

    @Autowired
    private TwitchUserService twitchUserService;

    @Autowired
    private DailyBonusRepository dailyBonusRepository;

    public boolean takeBonus(EventUser eventUser) {
        Long userId = Long.valueOf(eventUser.getId());
        TwitchUser user = twitchUserService.getTwitchUserById(userId);
        if (user == null) {
            user = twitchUserService.save(new TwitchUser(Long.valueOf(eventUser.getId()), eventUser.getName()));
        }
        if (user.getDailyBonus() == null) {
            user.setDailyBonus(new DailyBonus());
        }

        DailyBonus dailyBonus = user.getDailyBonus();
        LocalDate lastTimeUsed = dailyBonus.getLastTimeUsed();
        LocalDate now = LocalDate.now();
        if (now.isAfter(lastTimeUsed) && ChronoUnit.DAYS.between(lastTimeUsed, now) > 0) {
            dailyBonus.setPoints(dailyBonus.getPoints() + 1);
            dailyBonus.setLastTimeUsed(now);
            dailyBonus.setTwitchUser(user);
            dailyBonusRepository.save(dailyBonus);
            twitchUserService.save(user);
            return true;
        }
        return false;
    }

    public int getBonusCount(EventUser user) {
        TwitchUser twitchUser = twitchUserService.getTwitchUserById(Long.valueOf(user.getId()));
        if (twitchUser == null) {
            return 0;
        }
        if (twitchUser.getDailyBonus() == null) {
            twitchUser.setDailyBonus(new DailyBonus());
        }
        return twitchUser.getDailyBonus().getPoints();
    }

    public Long getPosition(EventUser user) {
        return dailyBonusRepository.getPosition(Long.valueOf(user.getId()));
    }
}
