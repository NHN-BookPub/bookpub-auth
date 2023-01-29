package com.nhnacademy.bookpubauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookpubauth.member.filter.CustomAuthenticationFilter;
import com.nhnacademy.bookpubauth.member.provider.CustomAuthenticationProvider;
import com.nhnacademy.bookpubauth.member.service.CustomUserDetailsService;
import com.nhnacademy.bookpubauth.token.service.TokenService;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security 설정 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private static final String LOGIN_REQUEST_MATCHER = "/auth/login";
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    /**
     * security filterChain 설정.
     *
     * @param http 간단하게 시큐리티 설정을 할 수있도록 제공해주는 파라미터.
     * @return 필터의 설정을 마친 후 필터체인을 리턴.
     * @throws Exception 필터가 작동되는 과정에서 발생되는 에러
     */
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

        http.addFilterAt(customAuthenticationFilter(null),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 유저의 비밀번호를 암호화, 검증 해주는 메소드 빈.
     *
     * @return BCryptEncoder를 반환.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * provider를 관리하는 authentication manager 메소드 빈.
     *
     * @param configuration authentication Manager를 관리하는 config 클래스.
     * @return authentication manager 반환.
     * @throws Exception authentication manager를 불러오는데 발행하는 에러들 처리.
     */
    @Bean
    public AuthenticationManager getAuthenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 로그인 정보를 받았을 때 인증서버의 dispatcher 이전에서 실행될 필터 메소드 빈.
     *
     * @param tokenService token 관련 서비스 로직이 담겨있다.
     * @return 커스텀하여 만든 UserPasswordAuthenticationFilter를 상속받는 필터를 반환.
     * @throws Exception getAuthenticationManager()를 사용할 때 발생하는 에러처리.
     */
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(
            TokenService tokenService) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter =
                new CustomAuthenticationFilter(customAuthenticationProvider(),
                        tokenService,
                        objectMapper);

        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_REQUEST_MATCHER);
        customAuthenticationFilter.setAuthenticationManager(getAuthenticationManager(null));

        return customAuthenticationFilter;
    }

    /**
     * 유저의 Authentication의 유효성을 검사하고 authenticate 발행하는 메소드 빈.
     * provider은 authentication manager의 구현체이다.
     * 커스텀 provider은 DaoAuthenticationProvider를 상속받아 구현되었다.
     *
     * @return 커스텀 provider 객체.
     */
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        CustomAuthenticationProvider provider
                = new CustomAuthenticationProvider();

        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

}
