package supportkim.shoppingmall.api.member;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supportkim.shoppingmall.api.dto.AlarmResponseDto;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.api.facade.LettuceLockQuantityFacade;
import supportkim.shoppingmall.api.facade.NamedLockQuantityFacade;
import supportkim.shoppingmall.api.facade.OptimisticLockQuantityFacade;
import supportkim.shoppingmall.api.facade.RedissonLockQuantityFacade;
import supportkim.shoppingmall.global.BaseResponse;
import supportkim.shoppingmall.service.AlarmService;
import supportkim.shoppingmall.service.CartService;
import supportkim.shoppingmall.service.CouponService;
import supportkim.shoppingmall.service.MemberService;

import static supportkim.shoppingmall.api.dto.AlarmResponseDto.*;
import static supportkim.shoppingmall.api.dto.CartResponseDto.*;
import static supportkim.shoppingmall.api.dto.CouponResponseDto.*;
import static supportkim.shoppingmall.api.dto.MemberRequestDto.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final CartService cartService;
    private final CouponService couponService;
    private final AlarmService alarmService;
    private final OptimisticLockQuantityFacade optimisticLockStockFacade;
    private final NamedLockQuantityFacade namedLockStockFacade;
    private final LettuceLockQuantityFacade lettuceLockStockFacade;
    private final RedissonLockQuantityFacade redissonLockQuantityFacade;

    // @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<MemberResponseDto.SignUp>> signUp(@RequestBody SignUp signUpDto) {
        return ResponseEntity.ok(new BaseResponse<>(memberService.signUp(signUpDto)));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<MemberResponseDto.SignUpWithAlarm>> signUpWithAlarm(@RequestBody SignUp signUpDto) {
        return ResponseEntity.ok(new BaseResponse<>(memberService.signUpWithAlarm(signUpDto)));
    }

    @GetMapping("/cart")
    public ResponseEntity<BaseResponse<getCart>> getCart(HttpServletRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(cartService.getCart(request)));
    }

    @GetMapping("/coupons")
    public ResponseEntity<BaseResponse<CouponList>> getCoupon(HttpServletRequest request) {
        return ResponseEntity.ok(new BaseResponse<>(couponService.getCouponList(request)));
    }

    @GetMapping("/alarm")
    public ResponseEntity<BaseResponse<MultiAlarm>> getAlarm (HttpServletRequest request) {
        return ResponseEntity.ok(new BaseResponse<> (alarmService.getAlarmList(request)));
    }


    /**
     * 동시성 테스트 로직
     */
    // 네고왕 전용 쿠폰 발급 (Issue Coupon)
    @PostMapping("/coupon/{coupon-id}")
    public ResponseEntity<BaseResponse<SingleCoupon>> issueCoupon(HttpServletRequest request ,
                                                                  @PathVariable("coupon-id") Long couponId)  {
        // 기본적인 로직 -> 2명의 사용자가 동시에 발급 받을 때 1개의 쿠폰만 나간다. (데이터 정합성 문제 발생)
        return ResponseEntity.ok(new BaseResponse<>(couponService.issueCoupon(request,couponId)));

        // JAVA 의 synchronized 사용 -> 250+ 동시 사용자가 생길시 오류 발생
        // return ResponseEntity.ok(new BaseResponse<>(couponService.issueCouponWithSynchronized(request,couponId)));

        // Database 의 비관적 락 사용 -> 300+ 동시 사용자가 생길시 오류 발생
        // return ResponseEntity.ok(new BaseResponse<>(couponService.issueCouponWithPermissionLock(request,couponId)));

        // Database 의 낙관적 락 사용 -> 5000+ 동시 사용자가 생길시 무한루프?
        // return ResponseEntity.ok(new BaseResponse<>(optimisticLockStockFacade.issueCouponWithOptimisticLock(request,couponId)));

        // Database 의 Named Lock 사용
        // return ResponseEntity.ok(new BaseResponse<>(namedLockStockFacade.issueCouponWithNamedLock(request,couponId)));

        // Redis 의 Lettuce Lock 사용 -> 무한루프..?
        // return ResponseEntity.ok(new BaseResponse<>(lettuceLockStockFacade.issueCouponWithLettuceLock(request,couponId)));

        // Redis 의 Redisson 사용
        // return ResponseEntity.ok(new BaseResponse<>(redissonLockQuantityFacade.issueCouponWithRedissonLock(request,couponId)));
    }

}
