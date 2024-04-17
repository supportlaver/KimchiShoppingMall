package supportkim.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import supportkim.shoppingmall.api.dto.MemberRequestDto;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.domain.Address;
import supportkim.shoppingmall.domain.Cart;
import supportkim.shoppingmall.domain.OrderKimchi;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.repository.MemberRepository;

import java.util.List;

import static supportkim.shoppingmall.api.dto.MemberRequestDto.*;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    public Long signUp(SignUp signUpDto) {
        Address address = Address.of(signUpDto);
        Member member = Member.of(signUpDto, address);
        signUpInitCart(member);
        // 비밀번호 암호화 후 저장
        member.setPasswordEncoder(passwordEncoder.encode(signUpDto.getPassword()));
        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    /**
     * 회원 가입시 한 회원에게 Cart 한 개를 할당하는 메서드
     */
    private static void signUpInitCart(Member member) {
        Cart cart = Cart.from();
        cart.initSingUp(member);
    }


}
