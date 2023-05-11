package com.nekromant.twitch.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class RedeemedPixels {
    @Id
    private String twitchUsername;

    @Column
    private Integer countPixels;
}
