package com.nekromant.twitch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Donat {
    @Id
    private Long id;
    @Column
    private String name;
    @Column
    private double amount;
    @Column
    private Instant timeDonat;
}
