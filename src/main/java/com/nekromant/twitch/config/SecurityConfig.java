package com.nekromant.twitch.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${twitch.security-config.adminName}")
    private String adminName;
    @Value("${twitch.security-config.adminPassword}")
    private String adminPassword;

    public SecurityConfig() {

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        try {
            auth.inMemoryAuthentication()
                    .withUser(adminName)
                    .password(adminPassword)
                    .roles("ADMIN");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/commands/**", "/songList/editor/**", "/dailyBonus/**").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and().formLogin()
                .loginPage("/login").defaultSuccessUrl("/commands", true).permitAll();
    }
}
