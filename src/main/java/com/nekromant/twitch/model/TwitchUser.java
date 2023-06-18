package com.nekromant.twitch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "twitch_users")
@Getter
@Setter
@NoArgsConstructor
public class TwitchUser {
    @Id
    private Long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "twitchUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TwitchUserMessage> messages = new ArrayList<>();

    @OneToOne(mappedBy = "twitchUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Kindness kindness;

    public TwitchUser(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
