package org.example.exceptions;

public class LocationExistsException extends RuntimeException {

    public LocationExistsException(String message) {
        super(message);
    }
}
