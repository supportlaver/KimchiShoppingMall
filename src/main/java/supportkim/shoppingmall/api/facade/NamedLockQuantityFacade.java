package supportkim.shoppingmall.api.facade;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.repository.LockRepository;
import supportkim.shoppingmall.service.CouponService;

import static supportkim.shoppingmall.api.dto.CouponResponseDto.*;

@Service
@RequiredArgsConstructor
public class NamedLockQuantityFacade {

    private final LockRepository lockRepository;

    private final CouponService couponService;


    @Transactional
    public SingleCoupon issueCouponWithNamedLock(HttpServletRequest request, Long id) {

        SingleCoupon singleCoupon;
        try {
            lockRepository.getLock(id.toString());
            singleCoupon = couponService.issueCouponWithNamedLock(request,id);
        } finally {
            lockRepository.releaseLock(id.toString());
        }

        return singleCoupon;
    }


}
