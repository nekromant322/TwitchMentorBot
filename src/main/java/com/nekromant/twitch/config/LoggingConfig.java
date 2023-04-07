package com.nekromant.twitch.config;

import com.nekromant.twitch.log.LoggingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(LoggingConfig.class)
public class LoggingConfig {

    @Bean
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }
}
