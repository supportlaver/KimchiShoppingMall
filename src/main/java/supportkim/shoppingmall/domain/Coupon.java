package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.member.Member;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class Coupon extends CouponBasedEntity{

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

}
