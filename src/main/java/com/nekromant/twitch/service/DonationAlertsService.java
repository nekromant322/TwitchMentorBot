package com.nekromant.twitch.service;

import com.nekromant.twitch.feign.DonationAlertsAuthFeign;
import com.nekromant.twitch.feign.DonationAlertsFeign;
import com.nekromant.twitch.mapper.DonatMapper;
import com.nekromant.twitch.mapper.DonationAlertsTokenMapper;
import com.nekromant.twitch.model.Donat;
import com.nekromant.twitch.model.DonationAlertsToken;
import com.nekromant.twitch.model.DonationResponse;
import com.nekromant.twitch.repository.DonationAlertsTokenRepository;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DonationAlertsService {
    @Autowired
    DonationAlertsTokenRepository donationAlertsTokenRepository;
    @Autowired
    private DonationAlertsAuthFeign donationAlertsAuthFeign;
    @Autowired
    private DonationAlertsFeign donationAlertsFeign;
    @Autowired
    private DonationAlertsTokenMapper donationAlertsTokenMapper;
    @Autowired
    private DonatMapper donatMapper;
    private static final int STATUS_CODE_SUCCESSFUL_EXECUTION = 200;

    public List<Donat> getDonations() {
        return getResponseFromDonationAlerts().getData()
                .stream()
                .map((dto) -> donatMapper.toDonat(dto))
                .collect(Collectors.toList());
    }

    private DonationResponse getResponseFromDonationAlerts() {
        try {
            return donationAlertsFeign.getDonations(" Bearer " + getAuthToken().getAccessToken());
        } catch (Exception e) {
            log.error("Токен DonationAlerts невалидный: " + e.getMessage());
            getAndSaveNewAuthTokenByRefreshToken();
            return donationAlertsFeign.getDonations(" Bearer " + getAuthToken().getAccessToken());
        }
    }

    public void getAndSaveNewAuthTokenByRefreshToken() {
        DonationAlertsToken token = getAuthToken();
        try {
            ResponseEntity<DonationAlertsToken> responseByRefreshToken = donationAlertsAuthFeign
                    .getNewTokenByRefreshToken(donationAlertsTokenMapper.toDonationAlertsTokenDTO(token));
            System.out.println(responseByRefreshToken);
            if (responseByRefreshToken.getStatusCodeValue() == STATUS_CODE_SUCCESSFUL_EXECUTION) {
                DonationAlertsToken newDonationAlertsToken = responseByRefreshToken.getBody();
                token.setAccessToken(newDonationAlertsToken.getAccessToken());
                token.setExpiresIn(newDonationAlertsToken.getExpiresIn());
                token.setRefreshToken(newDonationAlertsToken.getRefreshToken());
                donationAlertsTokenRepository.save(token);
                log.info("Обновление токена DonationAlerts выполнен");
            }
        } catch (FeignException e) {
            log.error(e.getMessage());
        }
    }

    public DonationAlertsToken getAuthToken() {
        return donationAlertsTokenRepository.findByType("auth");
    }
}
