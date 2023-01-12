package com.nhnacademy.bookpubauth.token.service;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

/**
 * 토큰 서비스.
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
    String tokenIssued(String userId,
                       Collection<? extends GrantedAuthority> authorities);

    /**
     * accessToken을 재발급해주는 메소드.
     *
     * @param userId      로그인한 유저의 아이디.
     * @param authorities 로그인한 유저의 권한들.
     * @return accessToken 재발급.
     */
    String tokenReIssued(String userId,
                         Collection<? extends GrantedAuthority> authorities);
}
