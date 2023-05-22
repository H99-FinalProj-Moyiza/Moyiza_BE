package com.example.moyiza_be.common.utils;

import lombok.Getter;

@Getter
public class Message {
    private final String message;

    public Message(String message) {
        this.message = message;
    }
}