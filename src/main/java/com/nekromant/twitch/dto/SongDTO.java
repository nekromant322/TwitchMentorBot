package com.nekromant.twitch.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongDTO {
    private Long id;

    private String artist;

    private String name;

    private String chordsLink;

    private String comment;
}
