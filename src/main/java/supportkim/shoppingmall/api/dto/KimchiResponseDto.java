package supportkim.shoppingmall.api.dto;

import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.Kimchi;

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
}
