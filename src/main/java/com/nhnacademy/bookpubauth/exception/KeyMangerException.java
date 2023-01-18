package com.nhnacademy.bookpubauth.exception;

/**
 * 키매니저 오류.
 *
 * @author : 유호철
 * @since : 1.0
 **/
public class KeyMangerException extends RuntimeException {
    public static final String MESSAGE = "키매니저가 뱉는 오류입니다 : ";

    public KeyMangerException(String message) {
        super(MESSAGE + message);
    }
}
