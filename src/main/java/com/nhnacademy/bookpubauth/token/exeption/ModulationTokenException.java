package com.nhnacademy.bookpubauth.token.exeption;

/**
 * 변조된 토큰일 경우 발생되는 exception
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class ModulationTokenException extends RuntimeException {
    public static final String MESSAGE = "변조된 토큰입니다.";

    public ModulationTokenException() {
        super(MESSAGE);
    }
}
