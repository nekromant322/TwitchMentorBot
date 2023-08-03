package com.nekromant.twitch.mapper;

import com.nekromant.twitch.dto.DailyBonusDTO;
import com.nekromant.twitch.dto.PointAucObjectDTO;
import com.nekromant.twitch.model.DailyBonus;
import org.springframework.stereotype.Component;

@Component
public class DailyBonusMapper {
    /** Константа-множитель для создания id объектов PointAucObjectDTO такого вида, который требуется pointauc`у */
    public static final Double POINT_AUC_ID_GENERATION_MULTIPLIER = 0.0000000000000001;

    public static DailyBonusDTO mapToDailyBonusDTO(DailyBonus dailyBonus) {
        return DailyBonusDTO.builder()
                .id(dailyBonus.getId())
                .lastTimeUsed(dailyBonus.getLastTimeUsed())
                .points(dailyBonus.getPoints())
                .twitchUserName(dailyBonus.getTwitchUser().getName())
                .build();
    }

    public static PointAucObjectDTO mapToPointAucObjectDTO(DailyBonus dailyBonus) {
        return PointAucObjectDTO.builder()
                .fastId(dailyBonus.getId())
                .id(dailyBonus.getId()*POINT_AUC_ID_GENERATION_MULTIPLIER)
                .amount(dailyBonus.getPoints())
                .name(dailyBonus.getTwitchUser().getName())
                .build();
    }
}
