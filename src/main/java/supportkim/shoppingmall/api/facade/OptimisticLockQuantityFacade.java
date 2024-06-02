package supportkim.shoppingmall.api.facade;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import supportkim.shoppingmall.service.CouponService;

import static supportkim.shoppingmall.api.dto.CouponResponseDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OptimisticLockQuantityFacade {

    private final CouponService couponService;

    // @Transactional
    public SingleCoupon issueCouponWithOptimisticLock(HttpServletRequest request , Long id)  {

        SingleCoupon singleCoupon = new SingleCoupon();

        while(true) {
            log.info("WHILE TRUE");
            try {
                singleCoupon = couponService.issueCouponWithOptimisticLock(request, id);

                break;
            }  catch (Exception e) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
        return singleCoupon;
    }
}
