package com.nhnacademy.bookpubauth.member.exception;

/**
 * 유저 dto 파싱 실패시 발생하는 exception.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class UserDtoParsingException extends RuntimeException {

    public static final String MESSAGE = "유저 정보를 파싱할 수 없습니다.";

    public UserDtoParsingException() {
        super(MESSAGE);
    }
}
