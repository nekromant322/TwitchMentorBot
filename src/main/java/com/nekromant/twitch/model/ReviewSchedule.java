package com.nekromant.twitch.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class ReviewSchedule {
    @Id
    private Long id;

    @Column()
    private String reviews;

    @Column()
    private LocalDateTime startTime;

    @Column()
    private LocalDateTime endTime;
}
