package com.nekromant.twitch.mapper;

import com.nekromant.twitch.dto.DonationAlertsTokenDTO;
import com.nekromant.twitch.model.DonationAlertsToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DonationAlertsTokenMapper {
    @Value("${donationAlerts.auth.clientId}")
    private String clientId;
    @Value("${donationAlerts.auth.secret}")
    private String secret;
    @Value("${donationAlerts.auth.grantType}")
    private String grantType;

    public DonationAlertsTokenDTO toDonationAlertsTokenDTO(DonationAlertsToken token){
        DonationAlertsTokenDTO donationAlertsTokenDTO = new DonationAlertsTokenDTO();
        donationAlertsTokenDTO.setGrant_type(grantType);
        donationAlertsTokenDTO.setRefresh_token(token.getRefreshToken());
        donationAlertsTokenDTO.setClient_id(clientId);
        donationAlertsTokenDTO.setClient_secret(secret);
        donationAlertsTokenDTO.setScope(token.getScope());

        return donationAlertsTokenDTO;
    }
}
