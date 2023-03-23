package com.nekromant.twitch.dto;

import lombok.Data;

@Data
public class BookedReviewDTO {
    private String studentUserName;
    private String studentTgLink;
    private String mentorUserName;
    private String title;
    private String bookedDateTime;
    private boolean tooLate;
    private String roomLink;
    private boolean today;

    public BookedReviewDTO(String studentUserName, String studentTgLink, String mentorUserName, String title,
                           String bookedDateTime, boolean tooLate, String roomLink, boolean today) {
        this.studentUserName = studentUserName;
        this.studentTgLink = studentTgLink;
        this.mentorUserName = mentorUserName;
        this.title = title;
        this.bookedDateTime = bookedDateTime;
        this.tooLate = tooLate;
        this.roomLink = roomLink;
        this.today = today;
    }
}
