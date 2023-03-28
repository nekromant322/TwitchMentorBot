package com.nekromant.twitch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchToken {
    @Id
    private Long id;

    @Column
    @JsonProperty(value = "access_token")
    private String accessToken;

    @Column
    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    @Column
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @Column
    private String type;
}
