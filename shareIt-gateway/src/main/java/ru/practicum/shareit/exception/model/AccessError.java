package ru.practicum.shareit.exception.model;

public class AccessError extends RuntimeException {
    public AccessError(String message) {
        super(message);
    }
}
