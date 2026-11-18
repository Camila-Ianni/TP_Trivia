package com.example.triviamaster.data.db;

public interface DbCallback<T> {
    void onSuccess(T result);

    void onError(Exception error);
}
