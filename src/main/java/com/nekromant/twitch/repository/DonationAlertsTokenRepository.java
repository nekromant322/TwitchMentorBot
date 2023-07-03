package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.DonationAlertsToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationAlertsTokenRepository extends CrudRepository<DonationAlertsToken, Long> {
    DonationAlertsToken findByType(String type);
}
