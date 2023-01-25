package com.nhnacademy.bookpubauth.member.adaptor;

import com.nhnacademy.bookpubauth.config.GateWayConfig;
import com.nhnacademy.bookpubauth.member.dto.MemberInfoRequestDto;
import com.nhnacademy.bookpubauth.member.dto.MemberInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * shop 서버와 통신하기 위한 어댑터 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAdaptor {
    private final GateWayConfig gateWayConfig;
    private final RestTemplate restTemplate;

    /**
     * shop 서버에 회원정보를 요청하는 메소드.
     *
     * @param requestDto 멤버 아이디 정보가 들어있는 회원정보 요청 dto
     * @return 회원정보를 응답받아 리턴해준다.
     */
    public ResponseEntity<MemberInfoResponseDto> loginRequest(
            MemberInfoRequestDto requestDto) {

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<MemberInfoRequestDto> entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.exchange(
                gateWayConfig.getGatewayUrl() + "/api/login",
                HttpMethod.POST,
                entity,
                MemberInfoResponseDto.class
        );
    }
}
