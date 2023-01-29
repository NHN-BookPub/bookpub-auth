package com.nhnacademy.bookpubauth.member.provider;

import com.nhnacademy.bookpubauth.member.exception.NotMatchMemberLoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

/**
 * 커스텀하여 만든 유저 인증 provider.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Slf4j
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {
    /**
     * 아이디와 패스워드로 인증하는 메소드.
     *
     * @param authentication token의 상위 클래스
     * @return token(jwt가 아닌 security 인증토큰)을 반환.
     * @throws AuthenticationException 인증 스트림에 대한 인증이 실패한 경우 throw되는 예외.
     */
    @Override
    public Authentication authenticate(
            Authentication authentication) throws AuthenticationException {
        String userId = (String) authentication.getPrincipal();
        String userPwd = (String) authentication.getCredentials();

        User user
                = (User) this.getUserDetailsService().loadUserByUsername(userId);

        boolean matches = this.getPasswordEncoder().matches(userPwd, user.getPassword());

        if (!matches) {
            throw new NotMatchMemberLoginInfo();
        }

        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities());
    }


}
