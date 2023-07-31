package com.nekromant.twitch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
