package supportkim.shoppingmall.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.service.MemberService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final MemberService memberService;
    @GetMapping("/")
    public String home(Model model) {

        Member member = findMemberFromSecurityContextHolder();
        int size=0;

        if (member==null) {
            size=0;
        } else{
            size = member.getCart().getCartItems().size();
        }
        model.addAttribute("size", size);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member check = memberService.findByLoginId(authentication.getName());
        if (check==null) {
            log.info("현재 로그인 상태가 아닙니다.");
        } else {
            log.info("현재 로그인 상태 입니다.");
        }
        return "home";
    }

    private Member findMemberFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Member member = memberService.findByLoginId(loginId);
        return member;
    }
}
