package com.nhnacademy.bookpubauth.token.util;

import java.time.Duration;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 토큰 발급 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "bookpub.jwt")
@Component
public class JwtUtil {
    private static final Long ACCESS_TOKEN_VALID_TIME = Duration.ofHours(1).toMillis();
    private static final Long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(1).toMillis();
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";

    private String secret;

    /**
     * 생성자 이전에 실행되는 초기화 메소드
     * 시크릿키는 Base64로 인코딩합니다.
     */
    @PostConstruct
    private void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * token 생성해주는 메소드.
     * <p>
     * claims에는 민감한 정보가 들어가면 안된다 유저의 아이디, 권한정도만 담아 shop에서 아이디로 조회, gateway에서 권한으로 인가정도 이용한다.
     *
     * @param userId         로그인한 유저의 아이디
     * @param authorities    로그인한 유저의 권한들
     * @param tokenType      발급할 토큰의 종류
     * @param tokenValidTime 발급할 토큰의 만료시간
     * @return jwt를 만들어 반환한다.
     */
    private String createToken(String userId,
                               Collection<? extends GrantedAuthority> authorities,
                               String tokenType,
                               Long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(tokenType);
        claims.put("userId", userId);
        claims.put("roles", authorities);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    /**
     * accessToken을 생성하는 메소드.
     *
     * @param userId      로그인한 유저 ID
     * @param authorities 로그인한 유저 권한들
     * @return accessToken 발급
     */
    public String createAccessToken(String userId, Collection<? extends GrantedAuthority> authorities) {
        return createToken(userId, authorities, ACCESS_TOKEN, ACCESS_TOKEN_VALID_TIME);
    }

    /**
     * refreshToken을 생성하는 메소드.
     *
     * @param userId      로그인한 유저 ID
     * @param authorities 로그인한 유저 권한들
     * @return refreshToken 발급
     */
    public String createRefreshToken(String userId,  Collection<? extends GrantedAuthority> authorities) {
        return createToken(userId, authorities, REFRESH_TOKEN, REFRESH_TOKEN_VALID_TIME);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
