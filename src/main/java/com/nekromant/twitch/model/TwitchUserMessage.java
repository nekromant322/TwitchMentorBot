package com.nekromant.twitch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "twitch_message")
@Getter
@Setter
@NoArgsConstructor
public class TwitchUserMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String message;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "twitchUser_id")
    private TwitchUser twitchUser;

    public TwitchUserMessage(String message) {
        this.message = message;
    }
}
