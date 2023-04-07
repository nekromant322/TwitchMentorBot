package com.nekromant.twitch.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class PixelState {
    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String matrix;
}
