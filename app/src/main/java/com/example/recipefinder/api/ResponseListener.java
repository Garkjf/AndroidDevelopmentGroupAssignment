package com.example.recipefinder.api;

/**
 * Interface for response listeners
 * @param <T> Type of response
 */
public interface ResponseListener<T> {
    /**
     * Called when an error occurs
     * @param message Error message
     */
    void onError(String message);

    /**
     * Called when a response is received
     * @param response The response received
     */
    void onResponse(T response);
}
