package com.nekromant.twitch.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DonationAlertsTokenDTO {
    private String grant_type;
    private String refresh_token;
    private String client_id;
    private String client_secret;
    private String scope;
}
