package com.nhnacademy.bookpubauth.member.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 토큰 반환 DTO 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponseDto {
    private Long memberNo;
    private String memberId;
    private String memberPwd;
    private List<String> authorities;
}
