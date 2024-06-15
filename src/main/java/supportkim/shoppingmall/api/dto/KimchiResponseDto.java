package supportkim.shoppingmall.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.domain.OrderKimchi;

import java.util.List;

public class KimchiResponseDto {

    @Builder @Getter
    public static class SingleKimchi {
        private Long id;
        private String name;
        public static SingleKimchi from(Kimchi kimchi) {
            return SingleKimchi.builder()
                    .id(kimchi.getId())
                    .name(kimchi.getName())
                    .build();
        }

        @JsonCreator
        public SingleKimchi(@JsonProperty("id") Long id , @JsonProperty("name") String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Builder @Getter
    public static class KimchiList {
        private List<SingleKimchi> kimchiList;

        public static KimchiList from(List<SingleKimchi> kimchiList) {
            return KimchiList.builder()
                    .kimchiList(kimchiList)
                    .build();
        }
    }

    @Builder @Getter
    public static class CartKimchi {
        // 장바구니에 담은 김치 종류
        private String name;
        // 수량
        private int count;
        // 총 가격
        private int totalPrice;

        public static CartKimchi from(OrderKimchi orderKimchi) {
            return CartKimchi.builder()
                    .count(orderKimchi.getCount())
                    .name(orderKimchi.getKimchi().getName())
                    .totalPrice(orderKimchi.getOrderPrice())
                    .build();
        }
    }
}
