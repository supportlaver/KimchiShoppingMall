package supportkim.shoppingmall.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KimchiRequestDto {

    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KimchiCart {
        // 구매하려고 하는 김치의 개수
        private int count;
    }
}
