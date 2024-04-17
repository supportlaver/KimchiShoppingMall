package supportkim.shoppingmall.api.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import supportkim.shoppingmall.api.dto.MemberRequestDto;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.global.BaseResponse;
import supportkim.shoppingmall.service.MemberService;

import static supportkim.shoppingmall.api.dto.MemberRequestDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<Long>> signUp(@RequestBody SignUp signUpDto) {
        return ResponseEntity.ok(new BaseResponse<>(memberService.signUp(signUpDto)));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Member member = memberService.findByLoginId(loginId);
        log.info("member = {} " , member);

        return ResponseEntity.ok("ok");
    }

}
