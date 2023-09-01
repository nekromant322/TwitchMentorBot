package com.nekromant.twitch.service;

import com.github.twitch4j.common.events.domain.EventUser;
import com.nekromant.twitch.dto.DailyBonusDTO;
import com.nekromant.twitch.dto.PointAucObjectDTO;
import com.nekromant.twitch.mapper.DailyBonusMapper;
import com.nekromant.twitch.model.DailyBonus;
import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.repository.DailyBonusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DailyBonusService {

    @Autowired
    private TwitchUserService twitchUserService;

    @Autowired
    private DailyBonusRepository dailyBonusRepository;

    public boolean takeBonus(EventUser eventUser) {
       return takeBonus(Long.valueOf(eventUser.getId()), eventUser.getName());
    }

    public boolean takeBonus(Long userId, String name) {
        TwitchUser user = twitchUserService.getTwitchUserById(userId);
        if (user == null) {
            user = twitchUserService.save(new TwitchUser(Long.valueOf(userId), name));
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

    public List<DailyBonusDTO> getDailyBonusDTOList() {
        return dailyBonusRepository.findAllOrderedByBonusAndTwitchUser().stream()
                .map(DailyBonusMapper::mapToDailyBonusDTO)
                .collect(Collectors.toList());
    }

    public List<PointAucObjectDTO> getPointAucObjectDTOList() {
        return dailyBonusRepository.findAllOrderedByBonusAndTwitchUser().stream()
                .map(DailyBonusMapper::mapToPointAucObjectDTO)
                .collect(Collectors.toList());
    }
}
