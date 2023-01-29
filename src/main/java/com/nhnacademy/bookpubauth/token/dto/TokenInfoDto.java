package com.nhnacademy.bookpubauth.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 토큰에 저장되어있는 정보를 저장하는 dto.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenInfoDto {
    private String sub;
    private String memberUuid;
    private String roles;
    private String iat;
    private Long exp;
}
