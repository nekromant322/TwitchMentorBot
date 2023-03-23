package com.nekromant.twitch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@PropertySource({"classpath:application.yml"})
@EnableFeignClients
@EnableScheduling
public class TwitchChatBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitchChatBotApplication.class, args);
	}

}
