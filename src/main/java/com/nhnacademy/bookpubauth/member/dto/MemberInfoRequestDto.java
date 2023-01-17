package com.nhnacademy.bookpubauth.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * shop 서버에 member 정보를 요청 할 DTO.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Getter
@AllArgsConstructor
public class MemberInfoRequestDto {
    String memberId;
}
