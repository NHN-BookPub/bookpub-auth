package com.nhnacademy.bookpubauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;

/**
 * 레디스를 이용하기위한 config 클래스.
 *
 * @author : 임태원
 * @since : 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "bookpub.redis")
@RequiredArgsConstructor
public class RedisConfig implements BeanClassLoaderAware {
    private final KeyConfig keyConfig;
    private String host;
    private String port;
    private String password;
    private String database;
    private ClassLoader classLoader;

    /**
     * 레디스에 연결하기 위한 configuration 설정 메소드 빈.
     *
     * @return 레디스 연결 설정이 들어간 Factory를 반환.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(keyConfig.keyStore(host));
        configuration.setPort(Integer.parseInt(keyConfig.keyStore(port)));
        configuration.setPassword(keyConfig.keyStore(password));
        configuration.setDatabase(Integer.parseInt(keyConfig.keyStore(database)));

        return new LettuceConnectionFactory(configuration);
    }

    /**
     * 레디스에 키, 밸류 등 값을 넣을 때, 뺄 때 직렬화, 역직렬화를 어떻게 할 지 설정하는 메소드.
     *
     * @return redis에 get,put 등을 할 수있게하는 redisTemplate객체를 반환.
     */
    @Bean
    @SuppressWarnings("java:S1452")
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(SecurityJackson2Modules.getModules(classLoader));

        return objectMapper;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
