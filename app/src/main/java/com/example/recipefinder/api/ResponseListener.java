package com.example.recipefinder.api;

public interface ResponseListener<T> {
    // Called when an error occurs
    void onError(String message);

    // Called when a response is received
    void onResponse(T response);
}
