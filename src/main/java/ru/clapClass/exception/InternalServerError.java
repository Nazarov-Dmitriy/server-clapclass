package ru.clapClass.exception;

import java.util.HashMap;

public class InternalServerError extends RuntimeException {
    private final HashMap<String, String> errors = new HashMap<>();

    public InternalServerError(String message) {
        super(message);
    }

    public InternalServerError(String message, String key) {
        super(message);
        this.errors.put(key, message);
    }
}
