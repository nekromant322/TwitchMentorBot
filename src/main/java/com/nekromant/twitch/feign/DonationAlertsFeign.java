package com.nekromant.twitch.feign;

import com.nekromant.twitch.model.DonationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "donationAlerts", url = "${donationAlerts.donations.url}")
public interface DonationAlertsFeign {
    @GetMapping(value = "${donationAlerts.donations.post}")
    DonationResponse getDonations(@RequestHeader("Authorization") String authorization);
}