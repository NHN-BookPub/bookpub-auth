package com.nhnacademy.bookpubauth.member.adaptor;

import com.nhnacademy.bookpubauth.config.GateWayConfig;
import com.nhnacademy.bookpubauth.member.dto.LoginMemberRequestDto;
import com.nhnacademy.bookpubauth.member.dto.LoginMemberResponseDto;
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

    public ResponseEntity<LoginMemberResponseDto> loginRequest(
            LoginMemberRequestDto requestDto) {
        log.info(requestDto.getMemberId());
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<LoginMemberRequestDto> entity = new HttpEntity<>(requestDto, headers);

        return restTemplate.exchange(
                gateWayConfig.getGatewayUrl() + "/api/login",
                HttpMethod.POST,
                entity,
                LoginMemberResponseDto.class
        );
    }
}
