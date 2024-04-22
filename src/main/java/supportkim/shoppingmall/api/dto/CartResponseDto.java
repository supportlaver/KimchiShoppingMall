package supportkim.shoppingmall.api.dto;

import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.OrderKimchi;

import java.util.List;

import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;

public class CartResponseDto {

    @Builder @Getter
    public static class getCart {
        private List<SingleKimchi> kimchis;
        private int count;

        public static getCart from(List<SingleKimchi> cartItems) {
            return getCart.builder()
                    .kimchis(cartItems)
                    .count(cartItems.size())
                    .build();
        }
    }
}
