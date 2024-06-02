package supportkim.shoppingmall.api.facade;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import supportkim.shoppingmall.repository.RedisLockRepository;
import supportkim.shoppingmall.service.CouponService;

import static supportkim.shoppingmall.api.dto.CouponResponseDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LettuceLockQuantityFacade {

    private final RedisLockRepository redisLockRepository;
    private final CouponService couponService;

    public SingleCoupon issueCouponWithLettuceLock(HttpServletRequest request , Long id) throws InterruptedException {
        log.info("LettuceLockStockFacade.issueCouponWithLettuceLock");
        // lock 획득 실패 시
        SingleCoupon singleCoupon;

        while(!redisLockRepository.lock(id)) {
            log.info("IN WHILE");
            // 100ms 이후 획득 재시도 -> 이렇게 해야 redis 에 가는 부하를 줄일 수 있다.
                Thread.sleep(100);

        }

        try {
            log.info("OUT WHILE");
            singleCoupon = couponService.issueCouponWithLettuceLock(request,id);
        } finally {
            redisLockRepository.unlock(id);
        }

        return singleCoupon;
    }
}
