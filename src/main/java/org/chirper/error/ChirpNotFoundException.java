package org.chirper.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Chirp not found!")
public class ChirpNotFoundException extends RuntimeException {

    private int statusCode;

    public ChirpNotFoundException() {
        this.statusCode = 404;
    }

    public ChirpNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}