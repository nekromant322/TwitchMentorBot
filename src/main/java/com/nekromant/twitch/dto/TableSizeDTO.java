package com.nekromant.twitch.dto;

import lombok.Data;

@Data
public class TableSizeDTO {
    private Integer width;
    private Integer height;

    public TableSizeDTO(Integer width, Integer height) {
        this.width = width;
        this.height = height;
    }
}
