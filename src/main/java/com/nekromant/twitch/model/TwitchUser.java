package com.nekromant.twitch.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "twitch_users")
@Getter
@Setter
@NoArgsConstructor
public class TwitchUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @OneToMany(mappedBy = "twitchUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TwitchUserMessage> messages = new ArrayList<>();

    public TwitchUser(String name) {
        this.name = name;
    }
}
