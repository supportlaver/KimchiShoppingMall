package supportkim.shoppingmall.api.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.Coupon;
import supportkim.shoppingmall.domain.CouponStatus;
import supportkim.shoppingmall.domain.member.Member;

import java.util.List;

public class CouponResponseDto {

    @Getter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class CouponList {
        private List<SingleCoupon> couponList;
        private int count;

        public static CouponList of(List<SingleCoupon> couponList) {
            return CouponList.builder()
                    .couponList(couponList)
                    .count(couponList.size())
                    .build();
        }


    }

    @Getter @Builder
    @NoArgsConstructor @AllArgsConstructor
    public static class SingleCoupon {
        private Long couponId;
        private CouponStatus couponStatus;
        private int discountValue;
        private String discountInfo;

        public SingleCoupon(Coupon c) {
            this.couponId = c.getId();
            this.couponStatus = c.getCouponStatus();
            this.discountValue = c.getDiscountValue();
            this.discountInfo = c.getDiscountInfo();
        }

    }
}
