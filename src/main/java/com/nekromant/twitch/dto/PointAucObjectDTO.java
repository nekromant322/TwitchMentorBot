package com.nekromant.twitch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointAucObjectDTO {
    private Long fastId;

    private Double id;

    private String extra;

    private Integer amount;

    private String name;
}
