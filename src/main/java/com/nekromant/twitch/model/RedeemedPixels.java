package com.nekromant.twitch.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RedeemedPixels {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String twitchUsername;
    @Column
    private Integer countPixels;
}
