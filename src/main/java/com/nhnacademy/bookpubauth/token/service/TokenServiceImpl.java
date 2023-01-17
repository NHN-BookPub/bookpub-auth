package com.nhnacademy.bookpubauth.token.service;

import com.nhnacademy.bookpubauth.token.util.JwtUtil;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
public class TokenServiceImpl implements TokenService {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String ACCESS_TOKEN = "access-token";

    /**
     * {@inheritDoc}
     */
    @Override
    public String tokenIssued(Long userNo,
                              Collection<? extends GrantedAuthority> authorities) {
        String memberUUID = UUID.randomUUID().toString();

        String refreshToken = jwtUtil.createRefreshToken(memberUUID, authorities);
        String accessToken = jwtUtil.createAccessToken(memberUUID, authorities);

        redisTemplate.opsForHash().put(String.valueOf(userNo), REFRESH_TOKEN, refreshToken);
        redisTemplate.opsForHash().put(memberUUID, ACCESS_TOKEN, String.valueOf(userNo));


        return accessToken;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String tokenReIssued(String userId, Collection<? extends GrantedAuthority> authorities) {
        return null;
    }

}
