package supportkim.shoppingmall.web.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MemberJoinForm {
    @NotEmpty(message = "아이디를 입력해주세요")
    private String loginId;
    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;
    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;
    @NotEmpty(message = "이름을 입력해주세요")
    private String name;
    @NotEmpty(message = "전화번호를 입력해주세요")
    private String phoneNumber;
    @NotEmpty(message = "도로명 주소를 입력해주세요")
    private String zipCode;
    @NotEmpty(message = "도로명 주소를 입력해주세요")
    private String streetCode;
    @NotEmpty(message = "상세주소를 입력해주세요")
    private String moreInfo;
    private String reference;
}
