package com.nhnacademy.bookpubauth.member.service;

import com.nhnacademy.bookpubauth.jwt.provider.JwtProvider;
import com.nhnacademy.bookpubauth.member.dto.LoginMemberResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 토큰 발급 로직이 처리되는 서비스클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    private final JwtProvider provider;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN = "refresh-token";

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginMemberResponseDto tokenIssued(String userId, List<String> authorities) {
        String refreshToken = provider.createRefreshToken(userId, authorities);
        redisTemplate.opsForHash().put(userId, REFRESH_TOKEN, refreshToken);

        return new LoginMemberResponseDto(provider.createAccessToken(userId, authorities));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginMemberResponseDto tokenReIssued(String userId, List<String> authorities) {
        return new LoginMemberResponseDto(provider.createAccessToken(userId, authorities));
    }
}
