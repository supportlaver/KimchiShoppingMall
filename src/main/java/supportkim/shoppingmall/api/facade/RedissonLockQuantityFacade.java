package supportkim.shoppingmall.api.facade;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import supportkim.shoppingmall.api.dto.CouponResponseDto;
import supportkim.shoppingmall.service.CouponService;

import java.util.concurrent.TimeUnit;

import static supportkim.shoppingmall.api.dto.CouponResponseDto.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockQuantityFacade {

    private final RedissonClient redissonClient;
    private final CouponService couponService;


    public SingleCoupon issueCouponWithRedissonLock(HttpServletRequest request , Long id) {
        RLock lock = redissonClient.getLock(id.toString());
        SingleCoupon singleCoupon = null;

        try {
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if (!available) {
                log.error("lock 획득 실패");
            }
            singleCoupon = couponService.issueCouponWithRedissonLock(request,id);

        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            lock.unlock();

        }
        return singleCoupon;
    }

}
