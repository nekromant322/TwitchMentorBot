package com.nekromant.twitch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyBonusDTO {
    private Long id;

    private LocalDate lastTimeUsed;

    private Integer points;

    private String twitchUser;
}