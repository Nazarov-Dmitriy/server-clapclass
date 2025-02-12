package ru.clapClass.exception;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class BadRequest extends RuntimeException {
    private final HashMap<String, String> errors = new HashMap<>();

    public BadRequest(String message, String key) {
        super(message);
        this.errors.put(key, message);
    }
}
