package com.nekromant.twitch.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity(name = "twitch_commands")
@Getter
@Setter
@NoArgsConstructor
public class TwitchCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String response;

    @Column
    private Long period;

    @Column
    private boolean enabled;

    @Column
    private Instant lastCompletionTime;

    public TwitchCommand(String name, String response) {
        this.name = name;
        this.response = response;
    }
}
