package com.nekromant.twitch.feign;


import com.nekromant.twitch.dto.DonationAlertsTokenDTO;
import com.nekromant.twitch.model.DonationAlertsToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "donationAlertsAuthFeign", url = "${donationAlerts.auth.url}")
public interface DonationAlertsAuthFeign {

    @PostMapping(value = "${donationAlerts.auth.post}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<DonationAlertsToken> getNewTokenByRefreshToken(@RequestBody DonationAlertsTokenDTO donationAlertsTokenDTO);
}

