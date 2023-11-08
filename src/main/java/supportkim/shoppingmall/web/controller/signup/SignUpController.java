package supportkim.shoppingmall.web.controller.signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import supportkim.shoppingmall.domain.member.Address;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.domain.member.Role;
import supportkim.shoppingmall.service.MemberService;
import supportkim.shoppingmall.web.form.MemberForm;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

    private final MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("memberForm",new MemberForm());
        return "/sign-up/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(MemberForm memberForm){
        Address address = Address.builder()
                .zipCode(memberForm.getZipCode())
                .reference(memberForm.getReference())
                .streetCode(memberForm.getStreetCode())
                .moreInfo(memberForm.getMoreInfo())
                .build();
        Member member = Member.builder()
                .name(memberForm.getName())
                .email(memberForm.getEmail())
                .phoneNumber(memberForm.getPhoneNumber())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .loginId(memberForm.getLoginId())
                .role(Role.ADMIN)
                .address(address)
                .build();
        memberService.join(member);
        return "/sign-up/sign-up-success";
    }
}
