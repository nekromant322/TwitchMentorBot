package com.nekromant.twitch.service;

import com.nekromant.twitch.cache.TwitchTokenUserCache;
import com.nekromant.twitch.dto.ValidationTokenDTO;
import com.nekromant.twitch.feign.TwitchAuthFeign;
import com.nekromant.twitch.model.RedeemedPixels;
import com.nekromant.twitch.repository.RedeemedPixelsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedeemedPixelsService {
    @Autowired
    private RedeemedPixelsRepository redeemedPixelsRepository;
    @Autowired
    private TwitchAuthFeign twitchAuthFeign;
    @Autowired
    private TwitchTokenUserCache twitchTokenUserCache;
    private final Integer NEW_USER_COUNT_PIXELS = 0;

    public Integer getRedeemedPixelsCountByToken(String token) {
        RedeemedPixels pixels = getByToken(token);
        if (pixels != null) {
            return pixels.getCountPixels();
        }
        save(getUsernameByToken(token), NEW_USER_COUNT_PIXELS);
        return NEW_USER_COUNT_PIXELS;
    }

    public void takeRedeemedPixel(String token) {
        RedeemedPixels pixels = getByToken(token);
        pixels.setCountPixels(pixels.getCountPixels() - 1);
        redeemedPixelsRepository.save(pixels);
    }

    public RedeemedPixels getByToken(String token) {
        String username = getUsernameByToken(token);
        return getByTwitchUsername(username);
    }

    public String getUsernameByToken(String token) {
        String username = twitchTokenUserCache.getUser(token);
        if (username != null) {
            return username;
        }

        ValidationTokenDTO dto = twitchAuthFeign.validateToken("OAuth " + token).getBody();
        twitchTokenUserCache.putCache(token, dto.getLogin());
        return dto.getLogin();
    }

    public RedeemedPixels getByTwitchUsername(String username) {
        return redeemedPixelsRepository.findByTwitchUsername(username);
    }

    public void save(String username, int count) {
        RedeemedPixels redeemedPixels = redeemedPixelsRepository.findByTwitchUsername(username);
        if (redeemedPixels == null) {
            redeemedPixels = new RedeemedPixels();
            redeemedPixels.setTwitchUsername(username);
            redeemedPixels.setCountPixels(count);
        } else {
            int oldCount = redeemedPixels.getCountPixels();
            redeemedPixels.setCountPixels(oldCount + count);
        }
        redeemedPixelsRepository.save(redeemedPixels);
    }
}
