package com.nekromant.twitch.service;

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

    public RedeemedPixels getByTwitchUsername(String username) {
        return redeemedPixelsRepository.findByTwitchUsername(username);
    }

    public RedeemedPixels getByToken(String token) {
        String username = getUsernameByToken(token);
        return getByTwitchUsername(username);
    }

    public RedeemedPixels save(String username, int count) {
        RedeemedPixels redeemedPixels = redeemedPixelsRepository.findByTwitchUsername(username);
        if (redeemedPixels == null) {
            redeemedPixels = new RedeemedPixels();
            redeemedPixels.setTwitchUsername(username);
            redeemedPixels.setCountPixels(count);
        } else {
            int oldCount = redeemedPixels.getCountPixels();
            redeemedPixels.setCountPixels(oldCount + count);
        }
        return redeemedPixelsRepository.save(redeemedPixels);
    }

    public String getUsernameByToken(String token) {
        ValidationTokenDTO dto = twitchAuthFeign.validateToken("OAuth " + token).getBody();
        return (dto != null) ? dto.getLogin() : null;
    }

    public Integer getRedeemedPixelsCountByToken(String token) {
        RedeemedPixels pixels = getByToken(token);
        return (pixels != null) ? pixels.getCountPixels() : null;
    }

    public RedeemedPixels takeRedeemedPixel(String token) {
        RedeemedPixels pixels = getByToken(token);
        pixels.setCountPixels(pixels.getCountPixels() - 1);
        return redeemedPixelsRepository.save(pixels);
    }
}
