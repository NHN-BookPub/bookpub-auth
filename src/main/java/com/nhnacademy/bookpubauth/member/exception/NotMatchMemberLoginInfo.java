package com.nhnacademy.bookpubauth.member.exception;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotMatchMemberLoginInfo extends RuntimeException {
    public static final String MESSAGE = "아이디 혹은 비밀번호가 틀렸습니다";
    public NotMatchMemberLoginInfo() {
        super(MESSAGE);
    }
}
