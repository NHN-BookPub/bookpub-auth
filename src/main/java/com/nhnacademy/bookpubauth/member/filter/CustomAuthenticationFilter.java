package com.nhnacademy.bookpubauth.member.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubauth.member.dto.LoginMemberRequestDto;
import com.nhnacademy.bookpubauth.member.exception.UserDtoParsingException;
import com.nhnacademy.bookpubauth.member.provider.CustomAuthenticationProvider;
import com.nhnacademy.bookpubauth.token.service.TokenService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * id, pwd를 입력받으면 jwt를 발급해주는 필터.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final CustomAuthenticationProvider provider;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;
    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer ";

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginMemberRequestDto requestDto;

        try {
            requestDto = objectMapper.readValue(request.getInputStream(), LoginMemberRequestDto.class);
        } catch (IOException e) {
            throw new UserDtoParsingException();
        }

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(requestDto.getId(), requestDto.getPwd());


        return provider.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String accessToken = tokenService.tokenIssued(
                (Long) authResult.getPrincipal(), authResult.getAuthorities());

        response.setHeader(AUTH_HEADER, TOKEN_TYPE + accessToken);
    }

}
