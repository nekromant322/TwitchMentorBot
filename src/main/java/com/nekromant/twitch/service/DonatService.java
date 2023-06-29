package com.nekromant.twitch.service;

import com.nekromant.twitch.model.Donat;
import com.nekromant.twitch.repository.DonatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DonatService {
    @Autowired
    private KindnessService kindnessService;
    @Autowired
    private DonatRepository donatRepository;
    @Autowired
    private DonationAlertsService donationAlertsService;

    public void addKindnessForDonat() {
        List<Donat> donatList = donationAlertsService.getDonations();
        Donat lastDonat = donatRepository.getLastDonat();
        if (lastDonat != null) {
            for (Donat donat : donatList) {
                if (donat.getTimeDonat().compareTo(lastDonat.getTimeDonat()) > 0) {
                    kindnessService.addKindnessForDonat(donat);
                    save(donat);
                }
            }
        } else {
            for (Donat donat : donatList) {
                kindnessService.addKindnessForDonat(donat);
            }
            donatRepository.saveAll(donatList);
        }
    }

    private void save(Donat donat) {
        donatRepository.save(donat);
    }
}
