package com.nhnacademy.bookpubauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Configuration
public class RestTemplateConfig {

    /**
     * 클라이언트와 서버 사이에 커넥션 객체를 생성, 소요 및 요청하는 최대시간 설정을 위한 클래스.
     *
     * @return ClientHttpRequestFactory의 구현체인 SimpleClientHttpRequestFactory를 반환한다.
     */
    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(3000);
        factory.setReadTimeout(100000);
        factory.setBufferRequestBody(false);

        return factory;
    }

    /**
     * 클라이언트와 서버간 요청, 응답하기 위한 RestTemplate 빈 설정.
     *
     * @param clientHttpRequestFactory 클라이언트와 서버간 커넥션 설정 factory class.
     * @return RestTemplate 반환.
     */
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }
}
