package supportkim.shoppingmall.web.form;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MemberForm {
    private String loginId;
    private String password;
    private String email;
    private String name;
    private String phoneNumber;
}
