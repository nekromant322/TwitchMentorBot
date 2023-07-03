package com.nekromant.twitch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DonationAlertsToken {
    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    @JsonProperty(value = "access_token")
    private String accessToken;

    @Column
    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    @Column(columnDefinition = "TEXT")
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @Column
    @JsonProperty(value = "scope_token")
    private String scope;

    @Column
    private String type;
}
