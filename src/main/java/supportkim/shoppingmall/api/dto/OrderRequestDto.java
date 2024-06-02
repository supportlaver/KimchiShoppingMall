package supportkim.shoppingmall.api.dto;

import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.OrderKimchi;

import java.util.List;

public class OrderRequestDto {

    @Builder @Getter
    public static class OrderKimchiList {
        private List<OrderKimchi> orderKimchiList;
    }
}
