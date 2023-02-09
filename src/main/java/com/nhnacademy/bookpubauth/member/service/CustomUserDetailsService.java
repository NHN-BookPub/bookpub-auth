package com.nhnacademy.bookpubauth.member.service;


import com.nhnacademy.bookpubauth.member.adaptor.MemberAdaptor;
import com.nhnacademy.bookpubauth.member.dto.MemberInfoRequestDto;
import com.nhnacademy.bookpubauth.member.dto.MemberInfoResponseDto;
import com.nhnacademy.bookpubauth.member.exception.NotMemberBookpubSite;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

/**
 * UserDetailService 클래스 상속받아 커스터마이징 클래스.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberAdaptor memberAdaptor;

    /**
     * 사용자 정보를 담는 인터페이스인 UserDetail을 반환해주는 메소드.
     *
     * @param userId 로그인 한 user의 아이디.
     * @return 사용자 정보가 담겨있는 UserDetail 클래스를 상속받은 Custom클래스.
     * @throws UsernameNotFoundException 가입하지 않은 계정으로 로그인 시도를 할 때 발생하는 exception.
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        ResponseEntity<MemberInfoResponseDto> responseMemberData;

        try {
            responseMemberData = memberAdaptor.loginRequest(new MemberInfoRequestDto(userId));
        } catch (HttpClientErrorException e) {
            throw new NotMemberBookpubSite();
        }

        MemberInfoResponseDto member = responseMemberData.getBody();

        List<SimpleGrantedAuthority> grantedAuthorities =
                member.getAuthorities().stream().map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new User(member.getMemberNo().toString(), member.getMemberPwd(), grantedAuthorities);
    }
}
