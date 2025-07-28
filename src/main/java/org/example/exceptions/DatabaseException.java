package org.example.exceptions;


public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
    }
}
