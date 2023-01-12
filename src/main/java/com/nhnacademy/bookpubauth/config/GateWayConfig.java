package com.nhnacademy.bookpubauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * GateWay 와 연동하기위한 Config 입니다.
 *
 * @author : 임태원
 * @since : 1.0
 **/
@Configuration
@ConfigurationProperties(prefix = "book-pub")
public class GateWayConfig {
    private String gateway;

    public String getGatewayUrl() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
}
