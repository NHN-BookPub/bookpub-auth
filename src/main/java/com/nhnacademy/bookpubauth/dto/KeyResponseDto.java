package com.nhnacademy.bookpubauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 키 매니저 반환 Dto.
 *
 * @author : 임태원, 유호철
 * @since : 1.0
 **/

@Getter
public class KeyResponseDto {
    private Header header;
    private Body body;


    /**
     * 키매니저의 바디값 클래스.
     */
    @Getter
    @NoArgsConstructor
    public static class Body {
        private String secret;
    }

    /**
     * 키매니저의 헤더 클래스.
     */
    @Getter
    @NoArgsConstructor
    public static class Header {
        private Integer resultCode;
        private String resultMessage;
        private boolean isSuccessful;
    }
}