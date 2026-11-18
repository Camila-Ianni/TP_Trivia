package com.example.triviamaster.data.db;

public class DuplicateRecordException extends Exception {
    public DuplicateRecordException(String message) {
        super(message);
    }
}
