package com.nhnacademy.bookpubauth.token.exeption;

/**
 * 토큰을 dto로 파싱하면서 발생될 수 있는 exception.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class TokenParsingException extends RuntimeException {
    public static final String MESSAGE = "토큰을 읽어올 수 없습니다";

    public TokenParsingException() {
        super(MESSAGE);
    }
}
