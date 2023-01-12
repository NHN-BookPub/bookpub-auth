package com.nhnacademy.bookpubauth.config;

import com.nhnacademy.bookpubauth.handler.LoginSuccessHandler;
import com.nhnacademy.bookpubauth.member.filter.CustomAuthenticationFilter;
import com.nhnacademy.bookpubauth.member.provider.CustomAuthenticationProvider;
import com.nhnacademy.bookpubauth.member.service.CustomUserDetailsService;
import com.nhnacademy.bookpubauth.token.service.TokenService;
import com.nhnacademy.bookpubauth.token.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 설정 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Slf4j
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private static final String LOGIN_REQUEST_MATCHER = "/auth/login";
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable();
        http.cors()
                .disable();
        http.formLogin()
                .disable();
        http.logout()
                .disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        customAuthenticationFilter().setAuthenticationSuccessHandler(
                loginSuccessHandler(null, null));

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(TokenService tokenService, JwtUtil jwtUtil) {
        return new LoginSuccessHandler(jwtUtil, tokenService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter
                = new CustomAuthenticationFilter(customAuthenticationProvider());
        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_REQUEST_MATCHER);
        customAuthenticationFilter.setAuthenticationManager(getAuthenticationManager(null));

        return customAuthenticationFilter;
    }
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider provider
                = new CustomAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

}
