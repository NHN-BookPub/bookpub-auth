package com.nhnacademy.bookpubauth.handler;

import com.nhnacademy.bookpubauth.token.service.TokenService;
import com.nhnacademy.bookpubauth.token.util.JwtUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer ";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String origin = request.getHeader("origin");
        log.error(origin);

        String accessToken = tokenService.tokenIssued(
                (String) authentication.getPrincipal(), authentication.getAuthorities());

        response.setHeader(AUTH_HEADER, TOKEN_TYPE + accessToken);
        response.sendRedirect(origin);
    }
}
