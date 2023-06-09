package com.nekromant.twitch.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data
@Entity(name = "twitch_message")
@Getter
@Setter
@NoArgsConstructor
public class TwitchUserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private String message;

    public TwitchUserMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }
}
