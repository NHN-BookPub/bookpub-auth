package com.nhnacademy.bookpubauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * auth 서버 메인.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class BookpubAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookpubAuthApplication.class, args);
    }

}
