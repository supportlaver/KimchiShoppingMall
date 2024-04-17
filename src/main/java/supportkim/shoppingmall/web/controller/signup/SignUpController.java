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
import supportkim.shoppingmall.domain.Cart;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.domain.member.Role;
import supportkim.shoppingmall.service.CartService;
import supportkim.shoppingmall.service.MemberService;
import supportkim.shoppingmall.web.form.MemberJoinForm;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignUpController {

    private final MemberService memberService;
    private final CartService cartService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 중복되는 아이디가 없어야 하기 때문에 이것을 위한 예외 처리가 필요하다.
//    @GetMapping("/sign-up")
    public String signUp(Model model) {
        model.addAttribute("memberForm",new MemberJoinForm());
        return "/sign-up/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUp(@Validated @ModelAttribute("memberForm") MemberJoinForm memberForm , BindingResult bindingResult){
        // 회원가입 할 때 한 회원에게 하나의 카트를 만들어서 넣어놓기

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

        // Setter 가 닫혀있기 떄문에 연관관계 편의 메서드를 안 만들고 처리 (메서드로 묶는 것도 고려)
        Cart cart = new Cart();
        member.setInitCart(cart);
        cart.setInitMember(member);
        memberService.join(member);
        return "/sign-up/sign-up-success";
    }
}
