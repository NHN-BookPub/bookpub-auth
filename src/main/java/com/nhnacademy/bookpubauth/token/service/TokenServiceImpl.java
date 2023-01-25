package com.nhnacademy.bookpubauth.token.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubauth.token.dto.TokenInfoDto;
import com.nhnacademy.bookpubauth.token.exeption.ModulationTokenException;
import com.nhnacademy.bookpubauth.token.exeption.TokenParsingException;
import com.nhnacademy.bookpubauth.token.exeption.UnusualApproachException;
import com.nhnacademy.bookpubauth.token.util.JwtUtil;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import io.jsonwebtoken.Claims;
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
    private static final String MESSAGE = "다시 로그인 하세요.";
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;


    /**
     * {@inheritDoc}
     */
    @Override
    public String tokenIssued(Long userNo,
                              Collection<? extends GrantedAuthority> authorities) {
        String memberUUID = UUID.randomUUID().toString();

        String refreshToken = jwtUtil.createRefreshToken(memberUUID, authorities);
        String accessToken = jwtUtil.createAccessToken(memberUUID, authorities);

        redisTemplate.opsForHash().put(JwtUtil.REFRESH_TOKEN, memberUUID, refreshToken);
        redisTemplate.opsForValue().set(memberUUID, String.valueOf(userNo));

        return accessToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String tokenReIssued(String accessToken) {
        Claims claims = jwtUtil.parseClaims(accessToken);
        String memberUUID = accessTokenValidate(claims);

        String refreshToken = getRefreshToken(memberUUID);

        String payload = jwtUtil.decodeJwt(refreshToken);
        TokenInfoDto tokenInfo = getTokenInfoDto(payload);

        long validTime = tokenInfo.getExp() - (new Date().getTime() / 1000);

        String message = refreshTokenExpCheck(validTime);
        if (Objects.nonNull(message)) return message;

        return jwtUtil.reissuedAccessToken(claims);
    }

    /**
     * redis에서 RefreshToken을 가져오는 메소드.
     *
     * @param memberUUID 멤버 고유번호.
     * @return 인증된 멤버의 refreshToken.
     */
    private String getRefreshToken(String memberUUID) {
        String refreshToken =
                (String) redisTemplate.opsForHash().get(JwtUtil.REFRESH_TOKEN, memberUUID);

        if (Objects.isNull(refreshToken)) {
            throw new UnusualApproachException();
        }
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
            return MESSAGE;
        }
        return null;
    }

    /**
     * tokenDto에 String인 payload를 파싱하여 값을 쉽게 건들기 위함.
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String accessTokenValidate(Claims claims) {
        String memberUUID = (String) claims.get("memberUUID");

        if (Objects.isNull(memberUUID)) {
            throw new ModulationTokenException();
        }

        return memberUUID;
    }

}
