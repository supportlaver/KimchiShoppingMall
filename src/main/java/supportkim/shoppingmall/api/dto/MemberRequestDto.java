package supportkim.shoppingmall.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class MemberRequestDto {

    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUp {
        private String loginId;
        private String password;
        private String email;
        private String name;
        private String phoneNumber;
        private String zipCode;
        private String streetCode;
        private String moreInfo;
        private String reference;
    }

    @Builder @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        private String loginId;
        private String password;
    }
}
