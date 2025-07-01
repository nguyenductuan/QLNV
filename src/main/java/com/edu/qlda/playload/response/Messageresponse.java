package com.edu.qlda.playload.response;

/**
 * A generic response wrapper class for API responses.
 * Includes status, message, and optional data.
 */
public class Messageresponse<T> {

    private int status;
    private String message;
    private T data;

    // Constructors
    public Messageresponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Messageresponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // Optional: toString() for logging/debugging
    @Override
    public String toString() {
        return "Messageresponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
