package supportkim.shoppingmall.global.config;

import io.lettuce.core.RedisURI;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import supportkim.shoppingmall.api.dto.KimchiRequestDto;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.api.facade.RedissonLockQuantityFacade;
import supportkim.shoppingmall.domain.Kimchi;

import static supportkim.shoppingmall.api.dto.KimchiRequestDto.*;
import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;
import static supportkim.shoppingmall.api.dto.MemberResponseDto.*;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    /**
     * Redis Template 설정 파일
     */


    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisProperties.getHost() , redisProperties.getPort());
    }

    // Kimchi 를 캐시하기 위한 Template
    // 이 부분하고 User 도 캐시하기
    @Bean
    public RedisTemplate<String , SingleKimchi> kimchiRedisTemplate() {
        RedisTemplate<String , SingleKimchi> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SingleKimchi.class));
        return redisTemplate;
    }

    // Member 를 캐시하기 위한 Template
    @Bean
    public RedisTemplate<String , SingleMember> memberRedisTemplate() {
        RedisTemplate<String , SingleMember> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SingleMember.class));
        return redisTemplate;
    }

}
