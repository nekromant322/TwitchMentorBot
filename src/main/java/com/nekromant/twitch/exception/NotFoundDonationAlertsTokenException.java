package com.nekromant.twitch.exception;

public class NotFoundDonationAlertsTokenException extends RuntimeException {
    public NotFoundDonationAlertsTokenException() {
    }
    public NotFoundDonationAlertsTokenException(String message) {
        super(message);
    }
}
