package com.nekromant.twitch.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyBonusDTO {
    private Long id;

    private LocalDate lastTimeUsed;

    private Integer points;

    private String twitchUserName;
}