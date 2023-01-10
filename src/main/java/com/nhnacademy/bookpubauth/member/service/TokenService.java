package com.nhnacademy.bookpubauth.member.service;

import com.nhnacademy.bookpubauth.member.dto.LoginMemberResponseDto;
import java.util.List;

/**
 * Some description here
 *
 * @author : 임태원
 * @since : 1.0
 **/
public interface TokenService {
    /**
     * 토큰을 발급해주는 메소드.
     *
     * @param userId      로그인한 유저의 아이디.
     * @param authorities 로그인한 유저의 권한들.
     * @return accessToken 발급
     */
    LoginMemberResponseDto tokenIssued(String userId, List<String> authorities);

    /**
     * accessToken을 재발급해주는 메소드.
     *
     * @param userId      로그인한 유저의 아이디.
     * @param authorities 로그인한 유저의 권한들.
     * @return accessToken 재발급.
     */
    LoginMemberResponseDto tokenReIssued(String userId, List<String> authorities);
}
