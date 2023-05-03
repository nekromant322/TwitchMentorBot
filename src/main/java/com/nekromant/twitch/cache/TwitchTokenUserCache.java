package com.nekromant.twitch.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
public class TwitchTokenUserCache {
    private final int CACHE_MAX_SIZE = 250;
    private final int CACHE_MAX_DAYS = 1;

    private final Cache<String, String> cache;

    @Autowired
    public TwitchTokenUserCache() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(CACHE_MAX_SIZE)
                .expireAfterWrite(CACHE_MAX_DAYS, TimeUnit.DAYS)
                .build();
    }

    public void putCache(String token, String username) {
        cache.put(token, username);
    }

    public String getUser(String token) {
        return cache.getIfPresent(token);
    }
}
