package com.nhnacademy.bookpubauth.member.exception;

/**
 * 로그인 하려는 정보와 db에 저장된 정보가 다를 시 발생되는 에러.
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
