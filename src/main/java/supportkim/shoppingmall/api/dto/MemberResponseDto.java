package supportkim.shoppingmall.api.dto;

import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.member.Member;

public class MemberResponseDto {

    @Builder @Getter
    public static class SignUp {
        private Long memberId;
    }

    @Builder @Getter
    public static class Login {
        private String name;
        private Long id;

        public static Login from(Member member) {
            return Login.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .build();
        }
    }
}
