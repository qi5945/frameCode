package com.xx.baseutilslibrary.network.exception;

/**
 * Created by JohnnyH on 2018/10/9.
 */

public class APILoginException extends RuntimeException {
    int errorCode;

    public APILoginException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
