package com.nekromant.twitch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PixelDTO {
    private Integer col;
    private Integer row;
    private String color;
}
