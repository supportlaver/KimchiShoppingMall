package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.global.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class Coupon extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private CouponStatus couponStatus;

    @Version
    private Long version;

    // 할인율? 할인값?
    private int discountValue;

    // 할인 종류 (할인 설명)
    private String discountInfo;

    private int quantity;

    // 회원가입 쿠폰 발급
    public static Coupon signUpCoupon() {
        return Coupon.builder()
                .couponStatus(CouponStatus.afterUSED)
                .discountValue(30)
                .discountInfo("회원가입 환영 :: 첫 주문 30% 할인 쿠폰")
                .build();
    }

    // 쿠폰 사용
    public void usedCoupon() {
        this.couponStatus = CouponStatus.beforeUSED;
    }

    // 쿠폰 수량 감소 (네고왕 전용 -> 한 장씩)
    public void decreaseQuantity() {
        if (this.quantity-1 < 0) {
            throw new BaseException(ErrorCode.EXCEEDED_QUANTITY_COUPON);
        }
        this.quantity -= 1;
    }
}
