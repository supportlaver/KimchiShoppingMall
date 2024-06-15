package supportkim.shoppingmall.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockRepository {

    private final RedisTemplate<String , String> redisTemplate;

    // 로직 실행 시 lock
    public Boolean lock(Long key) {

        return redisTemplate
                .opsForValue()
                // key 에는 couponID 가 들어가고 , value 에는 "lock" 이라는 문자가 들어간다.
                .setIfAbsent(generateKey(key) , "lock" , Duration.ofMillis(3_000));
    }

    // 로직 종료 시 unlock
    public Boolean unlock(Long key) {
        return redisTemplate.delete(generateKey(key));
    }


    private String generateKey(Long key) {
        return key.toString();
    }
}
