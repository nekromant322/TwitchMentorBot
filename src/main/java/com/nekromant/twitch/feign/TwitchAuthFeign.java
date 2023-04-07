package com.nekromant.twitch.feign;

import com.nekromant.twitch.dto.ValidationTokenDTO;
import com.nekromant.twitch.model.TwitchToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "twitchAuth", url = "https://id.twitch.tv/oauth2")
public interface TwitchAuthFeign {

    @GetMapping(value = "/validate")
    ResponseEntity<ValidationTokenDTO> validateToken(@RequestHeader(value = "Authorization") String authorizationHeader);

    @PostMapping(value = "/token?grant_type=refresh_token")
    ResponseEntity<TwitchToken> getNewTokenByRefreshToken(@RequestParam(value = "refresh_token") String refreshToken,
                                                          @RequestParam(value = "client_id") String clientId,
                                                          @RequestParam(value = "client_secret") String clientSecret);
}
