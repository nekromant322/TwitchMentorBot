package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.RedeemedPixels;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedeemedPixelsRepository extends CrudRepository<RedeemedPixels, Long> {

    RedeemedPixels findByTwitchUsername(String twitchUsername);
}
