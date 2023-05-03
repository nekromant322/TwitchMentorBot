package com.nekromant.twitch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PixelState {
    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String matrix;
}
