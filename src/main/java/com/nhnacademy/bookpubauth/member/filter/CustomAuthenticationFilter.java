package com.nhnacademy.bookpubauth.member.filter;

import com.nhnacademy.bookpubauth.member.provider.CustomAuthenticationProvider;
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

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String userId = this.obtainUsername(request);
        userId = userId != null ? userId.trim() : "";
        String pwd = this.obtainPassword(request);
        pwd = pwd != null ? pwd : "";

        log.warn(userId);
        log.warn(pwd);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(userId,pwd);

        return provider.authenticate(token);
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter("pwd");
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("id");
    }
}
