package supportkim.shoppingmall.web.controller.signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import supportkim.shoppingmall.domain.Address;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.domain.member.Role;
import supportkim.shoppingmall.service.MemberService;
import supportkim.shoppingmall.web.form.MemberJoinForm;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

    private final MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("memberForm",new MemberJoinForm());
        return "/sign-up/sign-up";
    }

//    @PostMapping("/sign-up")
//    public String signUp(MemberJoinForm memberForm){
//        Address address = Address.builder()
//                .zipCode(memberForm.getZipCode())
//                .reference(memberForm.getReference())
//                .streetCode(memberForm.getStreetCode())
//                .moreInfo(memberForm.getMoreInfo())
//                .build();
//        Member member = Member.builder()
//                .name(memberForm.getName())
//                .email(memberForm.getEmail())
//                .phoneNumber(memberForm.getPhoneNumber())
//                .password(passwordEncoder.encode(memberForm.getPassword()))
//                .loginId(memberForm.getLoginId())
//                .role(Role.ADMIN)
//                .address(address)
//                .build();
//        memberService.join(member);
//        return "/sign-up/sign-up-success";
//    }
    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute("memberForm") MemberJoinForm memberForm , BindingResult bindingResult){
        log.info("signUp In");

        if (bindingResult.hasErrors()){
            log.info("bindingResult In");
            return "/sign-up/sign-up";
        }
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
