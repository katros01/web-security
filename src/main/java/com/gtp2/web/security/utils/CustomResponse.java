package com.gtp2.web.security.utils;

import com.gtp2.web.security.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CustomResponse<T> {
    private String message;
    private int statusCode;
    private T data;

    public CustomResponse(String message, int statusCode, T data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }
}


