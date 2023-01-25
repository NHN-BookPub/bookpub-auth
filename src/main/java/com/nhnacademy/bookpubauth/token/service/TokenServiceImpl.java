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

        String refreshToken =
                (String) redisTemplate.opsForHash().get(JwtUtil.REFRESH_TOKEN, memberUUID);

        if (Objects.isNull(refreshToken)) {
            throw new UnusualApproachException();
        }

        redisTemplate.opsForHash().delete(accessToken, memberUUID);

        String payload = jwtUtil.decodeJwt(refreshToken);

        TokenInfoDto tokenInfo;
        try {
            tokenInfo = objectMapper.readValue(payload, TokenInfoDto.class);
        } catch (JsonProcessingException e) {
            throw new TokenParsingException();
        }

        long validTime = tokenInfo.getExp() - (new Date().getTime() / 1000);

        if (validTime <= 0) {
            return MESSAGE;
        }

        return jwtUtil.reissuedAccessToken(claims);
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
