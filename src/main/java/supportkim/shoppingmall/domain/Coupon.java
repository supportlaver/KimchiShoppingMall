package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.global.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class Coupon extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private CouponStatus couponStatus;

    // 할인율? 할인값?
    private int discountValue;

    // 할인 종류 (할인 설명)
    private String discountInfo;

    // 회원가입 쿠폰 발급
    public static Coupon signUpCoupon(Member member) {
        return Coupon.builder()
                .member(member)
                .couponStatus(CouponStatus.afterUSED)
                .discountValue(30)
                .discountInfo("회원가입 환영 :: 첫 주문 30% 할인 쿠폰")
                .build();
    }

    // 쿠폰 사용
    public void usedCoupon() {
        this.couponStatus = CouponStatus.beforeUSED;
    }
}
