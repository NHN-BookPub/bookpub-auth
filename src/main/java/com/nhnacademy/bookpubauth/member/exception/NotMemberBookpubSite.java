package com.nhnacademy.bookpubauth.member.exception;

/**
 * 회원정보가 없을 때 발생하는 에러.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class NotMemberBookpubSite extends RuntimeException {
    public static final String MESSAGE = "북펍의 회원이 아닙니다";

    public NotMemberBookpubSite() {
        super(MESSAGE);
    }
}
