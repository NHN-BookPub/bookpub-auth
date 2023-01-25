package com.nhnacademy.bookpubauth.token.exeption;

/**
 * 비 정상적으로 접근했을 때 발생하는 에러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class UnusualApproachException extends RuntimeException {
    public static final String MESSAGE = "비정상적인 접근 입니다.";

    public UnusualApproachException() {
        super(MESSAGE);
    }
}
