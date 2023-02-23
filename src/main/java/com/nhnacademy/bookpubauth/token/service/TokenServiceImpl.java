package com.nhnacademy.bookpubauth.token.service;

import static com.nhnacademy.bookpubauth.token.util.JwtUtil.TOKEN_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubauth.token.dto.TokenInfoDto;
import com.nhnacademy.bookpubauth.token.exeption.TokenParsingException;
import com.nhnacademy.bookpubauth.token.exeption.UnusualApproachException;
import com.nhnacademy.bookpubauth.token.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * 토큰 발급 로직이 처리되는 서비스클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private static final String EXP_MESSAGE = "다시 로그인 하세요.";
    private static final String TOKEN_INVALID_MESSAGE = "유효하지 않은 토큰입니다.";
    private static final String BLACK_LIST = "black_list";
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    /**
     * {@inheritDoc}
     */
    @Override
    public String tokenIssued(Long userNo,
                              Collection<? extends GrantedAuthority> authorities) {
        String memberUuid = UUID.randomUUID().toString();

        String refreshToken = jwtUtil.createRefreshToken(memberUuid, authorities);
        String accessToken = jwtUtil.createAccessToken(memberUuid, authorities);

        redisTemplate.opsForHash().put(JwtUtil.REFRESH_TOKEN, accessToken, refreshToken);
        redisTemplate.opsForValue().set(memberUuid, String.valueOf(userNo));

        return accessToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String tokenReIssued(String accessToken) {
        if (!jwtUtil.isValidateToken(accessToken)) {
            return TOKEN_INVALID_MESSAGE;
        }

        Claims claims = jwtUtil.parseClaims(accessToken);

        String refreshToken = getRefreshToken(accessToken);

        String payload = jwtUtil.decodeJwt(refreshToken);
        TokenInfoDto tokenInfo = getTokenInfoDto(payload);

        long validTime = tokenInfo.getExp() - (new Date().getTime() / 1000);

        String message = refreshTokenExpCheck(validTime);
        if (Objects.nonNull(message)) {
            return message;
        }
        String renewAccessToken = jwtUtil.reissuedAccessToken(claims);

        redisTemplate.opsForHash().put(JwtUtil.REFRESH_TOKEN, renewAccessToken, refreshToken);

        return renewAccessToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(String jwt) {
        String accessToken = jwt.substring(TOKEN_TYPE.length());
        Claims claims = jwtUtil.parseClaims(accessToken);
        long exp = claims.getExpiration().getTime();

        redisTemplate.opsForHash().put(BLACK_LIST, accessToken, "");
        redisTemplate.expire(accessToken, exp, TimeUnit.MICROSECONDS);
        redisTemplate.opsForHash().delete(JwtUtil.REFRESH_TOKEN, accessToken);
    }

    /**
     * redis에서 RefreshToken을 가져오는 메소드.
     *
     * @param accessToken 멤버 고유번호.
     * @return 인증된 멤버의 refreshToken.
     */
    private String getRefreshToken(String accessToken) {
        String refreshToken =
                (String) redisTemplate.opsForHash().get(JwtUtil.REFRESH_TOKEN, accessToken);
        if (Objects.isNull(refreshToken)) {
            throw new UnusualApproachException();
        }

        redisTemplate.opsForHash().delete(JwtUtil.REFRESH_TOKEN, accessToken);
        return refreshToken;
    }

    /**
     * refreshToken의 유지시간이 다 됐는지 체크하는 메소드.
     *
     * @param validTime 유효시간.
     * @return 유효한지에 대한 결과값.
     */
    private static String refreshTokenExpCheck(long validTime) {
        if (validTime <= 0) {
            return EXP_MESSAGE;
        }
        return null;
    }

    /**
     * tokenDto에 String인 payload를 파싱하여 값을 쉽게 건들기 위함.
     *
     * @param payload refreshToken의 payload 부분.
     * @return tokeninfo Dto.
     */
    private TokenInfoDto getTokenInfoDto(String payload) {
        TokenInfoDto tokenInfo;
        try {
            tokenInfo = objectMapper.readValue(payload, TokenInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new TokenParsingException();
        }
        return tokenInfo;
    }

}
