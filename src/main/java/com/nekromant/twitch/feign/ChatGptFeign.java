package com.nekromant.twitch.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "chatGpt", url = "${chatGPT.URL.client}")
public interface ChatGptFeign {
    @PostMapping(value = "${chatGPT.URL.POST}", consumes = MediaType.APPLICATION_JSON_VALUE)
    String getResponse(@RequestHeader("Authorization") String authorization, @RequestBody String requestBody);
}

