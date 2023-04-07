package com.nekromant.twitch.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidationTokenDTO {
    private String login;
    private Long userId;
}
