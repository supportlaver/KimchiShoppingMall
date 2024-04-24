package supportkim.shoppingmall.api.dto;

import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.Order;

public class OrderResponseDto {

    @Builder @Getter
    public static class CompleteOrder {
        private Long orderId;
        private String memberName;
        private int orderPrice;

        public static CompleteOrder of(Order order , int orderPrice) {
            return CompleteOrder.builder()
                    .memberName(order.getMember().getName())
                    .orderId(order.getId())
                    .orderPrice(orderPrice)
                    .build();
        }
    }

    @Builder @Getter
    public static class ApplyCouponOrder {
        private Integer discountPrice;
        private Integer applyCouponPrice;

        public static ApplyCouponOrder of(Integer discountPrice , Integer applyCouponPrice) {
            return ApplyCouponOrder.builder()
                    .applyCouponPrice(applyCouponPrice)
                    .discountPrice(discountPrice)
                    .build();
        }
    }
}
