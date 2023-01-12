package com.nhnacademy.bookpubauth.member.service;


import com.nhnacademy.bookpubauth.member.CustomUserDetails;
import com.nhnacademy.bookpubauth.member.adaptor.MemberAdaptor;
import com.nhnacademy.bookpubauth.member.dto.LoginMemberRequestDto;
import com.nhnacademy.bookpubauth.member.dto.LoginMemberResponseDto;
import com.nhnacademy.bookpubauth.member.exception.NotMemberBookpubSite;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberAdaptor memberAdaptor;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<LoginMemberResponseDto> responseMemberData
                = memberAdaptor.loginRequest(new LoginMemberRequestDto(username));

        if (Objects.isNull(responseMemberData)) {
            throw new NotMemberBookpubSite();
        }

        return new CustomUserDetails(responseMemberData.getBody());
    }
}
