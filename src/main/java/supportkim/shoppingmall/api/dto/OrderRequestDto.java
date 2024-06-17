package supportkim.shoppingmall.api.dto;

import lombok.*;
import supportkim.shoppingmall.domain.OrderKimchi;

import java.util.List;

public class OrderRequestDto {

    @Builder @Getter
    public static class OrderKimchiList {
        private List<OrderKimchi> orderKimchiList;
    }

    @Builder @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingleOrder {
        private int count;
    }
}
