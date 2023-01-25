package com.nhnacademy.bookpubauth.token.service;

import java.util.Collection;

import io.jsonwebtoken.Claims;
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
     * @param userNo      로그인한 유저의 PK.
     * @param authorities 로그인한 유저의 권한들.
     * @return accessToken 발급
     */
    String tokenIssued(Long userNo,
                       Collection<? extends GrantedAuthority> authorities);


    /**
     * accessToken을 재발급해주는 메소드.
     *
     * @param accessToken 재발급 받으려고 하는 accessToken
     * @return accessToken 재발급.
     */
    String tokenReIssued(String accessToken);

    /**
     * accessToken이 변조된 게 아닌지 확인하는 메소드.
     * @param claims 서버에서 전송 된 accessToken의 claim부분.
     */
    String accessTokenValidate(Claims claims);
}
