package com.nekromant.twitch.service;

import com.nekromant.twitch.cache.TokenUser;
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
    private TokenUser tokenUser;


    public Integer getRedeemedPixelsCountByToken(String token) {
        RedeemedPixels pixels = getByToken(token);
        return (pixels != null) ? pixels.getCountPixels() : null;
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
        if (tokenUser.getUser(token) != null) {
            return tokenUser.getUser(token);
        }

        ValidationTokenDTO dto = twitchAuthFeign.validateToken("OAuth " + token).getBody();
        if (dto != null) {
            tokenUser.putCache(token, dto.getLogin());
            return dto.getLogin();
        }
        return null;
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
