package supportkim.shoppingmall.service;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.domain.Coupon;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.CouponRepository;
import supportkim.shoppingmall.repository.MemberRepository;

import java.util.List;

import static java.util.stream.Collectors.*;
import static supportkim.shoppingmall.api.dto.CouponResponseDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public CouponList getCouponList(HttpServletRequest request) {

        Member member = findMemberFromAccessToken(request);
        List<SingleCoupon> coupons = member.getCoupons()
                .stream().map(SingleCoupon::new)
                .collect(toList());
        return CouponList.of(coupons);

    }

    /**
     * 일반적인 로직
     */
    @Transactional
    public SingleCoupon issueCoupon(HttpServletRequest request , Long couponId) {
        Member member = findMemberFromAccessToken(request);
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_COUPON));

        // 수량 1개 감소
        coupon.decreaseQuantity();

        return new SingleCoupon(coupon);
    }

    /**
     * JAVA 의 synchronized 사용
     */
    // @Transactional
    public synchronized SingleCoupon issueCouponWithSynchronized(HttpServletRequest request , Long couponId) {
        Member member = findMemberFromAccessToken(request);
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_COUPON));

        // 수량 1개 감소
        coupon.decreaseQuantity();

        // @Transactional 로 인해 동시서 문제가 또 발생하기 때문에 couponRepository.flush() 로 직접 DB 에 반영
        couponRepository.flush();

        return new SingleCoupon(coupon);
    }

    /**
     * Database 의 비관적 락 사용
     */
    @Transactional
    public SingleCoupon issueCouponWithPermissionLock(HttpServletRequest request , Long couponId) {
        Member member = findMemberFromAccessToken(request);
        Coupon coupon = couponRepository.findByIdWithPermissionLock(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_COUPON));

        // 수량 1개 감소
        coupon.decreaseQuantity();

        return new SingleCoupon(coupon);
    }

    /**
     * Database 의 낙관적 락 사용
     */
    @Transactional
    public SingleCoupon issueCouponWithOptimisticLock(HttpServletRequest request , Long couponId) {
        Member member = findMemberFromAccessToken(request);
        Coupon coupon = couponRepository.findByIdWithOptimisticLock(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_COUPON));

        // 수량 1개 감소
        coupon.decreaseQuantity();

        return new SingleCoupon(coupon);
    }

    /**
     * Database 의 Named Lock 사용
     */
    // 부모의 트랜잭션과 별도로 실행되어야 하기 때문에 전파 전략 변경
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SingleCoupon issueCouponWithNamedLock(HttpServletRequest request , Long couponId) {
        Member member = findMemberFromAccessToken(request);
        Coupon coupon = couponRepository.findByIdWithOptimisticLock(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_COUPON));

        // 수량 1개 감소
        coupon.decreaseQuantity();

        return new SingleCoupon(coupon);
    }

    /**
     * Redis 의 LettuceLock 사용
     */
    // 부모의 트랜잭션과 별도로 실행되어야 하기 때문에 전파 전략 변경
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SingleCoupon issueCouponWithLettuceLock(HttpServletRequest request , Long couponId) {
        log.info("CouponService.issueCouponWithLettuceLock");
        Member member = findMemberFromAccessToken(request);
        Coupon coupon = couponRepository.findByIdWithOptimisticLock(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_COUPON));

        // 수량 1개 감소
        coupon.decreaseQuantity();

        return new SingleCoupon(coupon);
    }

    /**
     * Redis 의 Redisson 사용
     */
    // 부모의 트랜잭션과 별도로 실행되어야 하기 때문에 전파 전략 변경
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SingleCoupon issueCouponWithRedissonLock(HttpServletRequest request , Long couponId) {
        log.info("CouponService.issueCouponWithLettuceLock");
        Member member = findMemberFromAccessToken(request);
        Coupon coupon = couponRepository.findByIdWithOptimisticLock(couponId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_COUPON));

        // 수량 1개 감소
        coupon.decreaseQuantity();

        return new SingleCoupon(coupon);
    }



    public Integer getCouponStock() {
        Coupon coupon = couponRepository.findById(1L).orElseThrow();
        return coupon.getQuantity();
    }

    private Member findMemberFromAccessToken(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ACCESS_TOKEN));
        String email = jwtService.extractMemberEmail(accessToken);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
    }

}
