package com.nekromant.twitch.service;

import com.nekromant.twitch.model.TwitchToken;
import com.nekromant.twitch.feign.TwitchAuthFeign;
import com.nekromant.twitch.repository.TwitchTokenRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TwitchAuthService {
    @Value("${twitch.botClientId}")
    private String botClientId;
    @Value("${twitch.botSecret}")
    private String botSecret;

    @Autowired
    private TwitchAuthFeign twitchAuthFeign;
    @Autowired
    private TwitchTokenRepository twitchTokenRepository;

    @Scheduled(initialDelayString = "PT01H" , fixedDelayString = "PT01H")
    public String getAuthToken() {
        TwitchToken token = twitchTokenRepository.findFirstById(1L);

        if (token != null) {
            if (validateToken(token)) {
                return token.getAccessToken();
            } else {
                String refreshToken = token.getRefreshToken();
                return getNewAuthTokenByRefreshToken(refreshToken);
            }
        }

        return null;
    }


    public boolean validateToken(TwitchToken token) {
        String accessToken = token.getAccessToken();

        try {
            ResponseEntity<?> response = twitchAuthFeign.validateToken(getAuthHeader(accessToken));
            if (response.getStatusCodeValue() == 200) {
                System.out.println("Токен валидный - " + LocalDateTime.now().toLocalTime().toString());
                return true;
            }
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public String getNewAuthTokenByRefreshToken(String refreshToken) {
        try {
            ResponseEntity<TwitchToken> responseByRefreshToken = twitchAuthFeign.getNewTokenByRefreshToken(refreshToken, botClientId, botSecret);
            if (responseByRefreshToken.getStatusCodeValue() == 200) {
                TwitchToken newTwitchToken = responseByRefreshToken.getBody();
                String newToken = newTwitchToken.getAccessToken();
                saveNewToken(newTwitchToken);
                return newToken;
            }
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private String getAuthHeader(String accessToken) {
        return "OAuth " + accessToken;
    }

    private void saveNewToken(TwitchToken twitchToken) {
        twitchToken.setId(1L);
        twitchTokenRepository.save(twitchToken);
    }
}
