package com.nekromant.twitch.exception;

public class UnauthorizedDonationAlertsTokenException extends RuntimeException {
    public UnauthorizedDonationAlertsTokenException() {
    }
    public UnauthorizedDonationAlertsTokenException(String message) {
        super(message);
    }
}
