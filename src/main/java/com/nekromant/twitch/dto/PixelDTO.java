package com.nekromant.twitch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PixelDTO {
    private int col;
    private int row;
    private String color;
}
