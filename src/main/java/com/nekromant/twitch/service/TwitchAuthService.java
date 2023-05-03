package com.nekromant.twitch.service;

import com.nekromant.twitch.dto.ValidationTokenDTO;
import com.nekromant.twitch.feign.TwitchAuthFeign;
import com.nekromant.twitch.model.TwitchToken;
import com.nekromant.twitch.repository.TwitchTokenRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TwitchAuthService {
    @Value("${twitch.auth.botClientId}")
    private String botClientId;
    @Value("${twitch.auth.botSecret}")
    private String botSecret;
    @Value("${twitch.auth.moderationClientId}")
    private String moderationClientId;
    @Value("${twitch.auth.moderationSecret}")
    private String moderationSecret;

    @Autowired
    private TwitchAuthFeign twitchAuthFeign;
    @Autowired
    private TwitchTokenRepository twitchTokenRepository;

    public TwitchToken getAuthToken() {
        return twitchTokenRepository.findByType("auth");
    }

    public TwitchToken getModerationToken() {
        return twitchTokenRepository.findByType("moderation");
    }

    public TwitchToken getAndSaveNewModerationToken() {
        TwitchToken token = getModerationToken();
        String refreshToken = token.getRefreshToken();
        try {
            ResponseEntity<TwitchToken> responseByRefreshToken = twitchAuthFeign.getNewTokenByRefreshToken(refreshToken, moderationClientId, moderationSecret);
            if (responseByRefreshToken.getStatusCodeValue() == 200) {
                TwitchToken newTwitchToken = responseByRefreshToken.getBody();
                token.setAccessToken(newTwitchToken.getAccessToken());
                token.setExpiresIn(newTwitchToken.getExpiresIn());
                token.setRefreshToken(newTwitchToken.getRefreshToken());
                twitchTokenRepository.save(token);
                return token;
            }
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean validateToken(TwitchToken token) {
        String accessToken = token.getAccessToken();

        try {
            ResponseEntity<ValidationTokenDTO> response = twitchAuthFeign.validateToken(getAuthHeader(accessToken));
            if (response.getStatusCodeValue() == 200) {
                System.out.println("Токен валидный - " + LocalDateTime.now().toLocalTime().toString());
                return true;
            }
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public TwitchToken getAndSaveNewAuthTokenByRefreshToken(String refreshToken) {
        try {
            ResponseEntity<TwitchToken> responseByRefreshToken = twitchAuthFeign.getNewTokenByRefreshToken(refreshToken, botClientId, botSecret);
            if (responseByRefreshToken.getStatusCodeValue() == 200) {
                TwitchToken newTwitchToken = responseByRefreshToken.getBody();
                TwitchToken token = getAuthToken();
                token.setAccessToken(newTwitchToken.getAccessToken());
                token.setExpiresIn(newTwitchToken.getExpiresIn());
                token.setRefreshToken(newTwitchToken.getRefreshToken());
                twitchTokenRepository.save(token);
                return newTwitchToken;
            }
        } catch (FeignException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    private String getAuthHeader(String accessToken) {
        return "OAuth " + accessToken;
    }
}
