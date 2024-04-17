package supportkim.shoppingmall.api.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(@RequestBody Login loginDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(new BaseResponse<>("ok"));
    }

}
