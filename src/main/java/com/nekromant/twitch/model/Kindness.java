package com.nekromant.twitch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Kindness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Instant lastUse;
    private double indexKindness;
    private double lengthMessages;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "twitchUser_id")
    private TwitchUser twitchUser;

    public Kindness(Instant lastUse, double indexKindness, double lengthMessages) {
        this.lastUse = lastUse;
        this.indexKindness = indexKindness;
        this.lengthMessages = lengthMessages;
    }
}
