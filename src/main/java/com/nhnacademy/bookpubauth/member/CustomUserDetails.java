package com.nhnacademy.bookpubauth.member;

import com.nhnacademy.bookpubauth.member.dto.MemberInfoResponseDto;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 유저정보를 담고있는 userDetail interface 의 상속체.
 *
 * @author : 임태원
 * @since : 1.0
 **/
public class CustomUserDetails implements UserDetails {
    private final MemberInfoResponseDto member;

    public CustomUserDetails(MemberInfoResponseDto member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getAuthorities().stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public Long getMemberNo() {
        return member.getMemberNo();
    }

    @Override
    public String getPassword() {
        return member.getMemberPwd();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
