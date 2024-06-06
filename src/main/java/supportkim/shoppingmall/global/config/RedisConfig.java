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
import org.springframework.data.redis.serializer.StringRedisSerializer;
import supportkim.shoppingmall.api.facade.RedissonLockQuantityFacade;

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

    @Bean
    @Primary
    public RedisTemplate<String , Object> redisTemplate() {
        RedisTemplate<String , Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
