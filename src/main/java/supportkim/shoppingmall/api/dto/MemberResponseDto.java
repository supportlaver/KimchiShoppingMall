package supportkim.shoppingmall.api.dto;

import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.jwt.TokenMapping;

public class MemberResponseDto {

    @Builder @Getter
    public static class SignUp {
        private Long memberId;
    }

    @Builder @Getter
    public static class Login {
        private String name;
        private Long id;
        private TokenMapping tokenMapping;

        public static Login from(Member member , TokenMapping tokenMapping) {
            return Login.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .tokenMapping(tokenMapping)
                    .build();
        }
    }
}
