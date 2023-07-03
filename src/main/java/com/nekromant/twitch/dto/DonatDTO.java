package com.nekromant.twitch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DonatDTO {
    private int id;
    private String username;
    private String amount;
    private String created_at;
    public String getUsername() {
        return this.username = (username != null) ? username : "";
    }
}
