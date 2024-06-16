package supportkim.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.MemberRequestDto;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.domain.*;
import supportkim.shoppingmall.domain.alarm.AlarmType;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.repository.CouponRepository;
import supportkim.shoppingmall.repository.MemberRepository;

import java.util.List;

import static supportkim.shoppingmall.api.dto.MemberRequestDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CouponRepository couponRepository;
    private final AlarmService alarmService;

    public Member join(Member member) {
        memberRepository.save(member);
        return member;
    }

    public Member findByLoginId(String loginId){
        Member findMember = memberRepository.findByLoginId(loginId);
        return findMember;
    }

    /**
     *
     * 아래부터는 API 통신을 위한 MemberService 코드 입니다.
     *
     */
    @Transactional
    public MemberResponseDto.SignUp signUp(SignUp signUpDto) {
        Address address = Address.of(signUpDto);
        Member member = Member.of(signUpDto, address);
        signUpInitCart(member);
        // 비밀번호 암호화 후 저장
        member.setPasswordEncoder(passwordEncoder.encode(signUpDto.getPassword()));
        Member savedMember = memberRepository.save(member);
        Coupon coupon = Coupon.signUpCoupon();
        couponRepository.save(coupon);
        return MemberResponseDto.SignUp.from(member,coupon);
    }

    @Transactional
    public MemberResponseDto.SignUpWithAlarm signUpWithAlarm(SignUp signUpDto) {
        Address address = Address.of(signUpDto);
        Member member = Member.of(signUpDto, address);
        signUpInitCart(member);
        // 비밀번호 암호화 후 저장
        member.setPasswordEncoder(passwordEncoder.encode(signUpDto.getPassword()));
        Member savedMember = memberRepository.save(member);
        Coupon coupon = Coupon.signUpCoupon();
        alarmService.send(AlarmType.CONGRATULATION_SIGN_UP , savedMember);
        couponRepository.save(coupon);
        return MemberResponseDto.SignUpWithAlarm.from(member,coupon,AlarmType.CONGRATULATION_SIGN_UP);
    }

    /**
     * 회원 가입시 한 회원에게 Cart 한 개를 할당하는 메서드
     */
    private static void signUpInitCart(Member member) {
        Cart cart = Cart.from();
        cart.initSingUp(member);
    }


}
