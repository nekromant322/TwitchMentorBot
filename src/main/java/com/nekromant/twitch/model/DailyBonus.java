package com.nekromant.twitch.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DailyBonus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate lastTimeUsed = LocalDate.EPOCH;

    @Column
    private Integer points = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "twitchUser_id")
    private TwitchUser twitchUser;
}