package com.nhnacademy.bookpubauth.token.util;

import com.nhnacademy.bookpubauth.config.KeyConfig;
import java.time.Duration;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * 토큰 발급 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "bookpub.jwt")
@Component
@Slf4j
public class JwtUtil {
    public static final Long ACCESS_TOKEN_VALID_TIME = Duration.ofHours(1).toMillis();
    private static final Long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(1).toMillis();
    public static final String ACCESS_TOKEN = "access-token";
    public static final String REFRESH_TOKEN = "refresh-token";
    public static final String AUTH_HEADER = "Authorization";
    public static final String EXP_HEADER = "X-Expire";
    public static final String TOKEN_TYPE = "Bearer ";
    private final Base64.Decoder decoder = Base64.getUrlDecoder();
    private final KeyConfig keyConfig;
    private String secret;


    /**
     * 생성자 이전에 실행되는 초기화 메소드
     * 시크릿키는 Base64로 인코딩합니다.
     */
    @PostConstruct
    private void init() {
        secret = keyConfig.keyStore(secret);
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
        claims.put("memberUUID", userId);
        claims.put("roles", authorities.toString());
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
     * @param memberUUID  로그인한 유저 ID
     * @param authorities 로그인한 유저 권한들
     * @return accessToken 발급
     */
    public String createAccessToken(String memberUUID, Collection<? extends GrantedAuthority> authorities) {
        return createToken(memberUUID, authorities, ACCESS_TOKEN, ACCESS_TOKEN_VALID_TIME);
    }

    /**
     * refreshToken을 생성하는 메소드.
     *
     * @param memberUUID  로그인한 유저 ID
     * @param authorities 로그인한 유저 권한들
     * @return refreshToken 발급
     */
    public String createRefreshToken(String memberUUID, Collection<? extends GrantedAuthority> authorities) {
        return createToken(memberUUID, authorities, REFRESH_TOKEN, REFRESH_TOKEN_VALID_TIME);
    }

    /**
     * claim부분만 파싱해주는 메소드.
     *
     * @param token jwt토큰.
     * @return jwt의 claim부분만 파싱 된 결과.
     */
    public Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * accessToken 재발급 메소드.
     *
     * @param claims 발급되었던 token의 claim 부분.
     * @return accessToken 발급.
     */
    public String reissuedAccessToken(Claims claims) {
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secret.getBytes())
                .compact();
    }

    /**
     * 토큰을 복호화해주는 메소드.
     *
     * @param jwt accessToken 정보.
     * @return 복호화 된 정보를 리턴해준다.
     */
    public String decodeJwt(String jwt) {
        String jsonWebToken = jwt.substring(TOKEN_TYPE.length());
        String payload = jsonWebToken.split("\\.")[1];

        return new String(decoder.decode(payload));
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
