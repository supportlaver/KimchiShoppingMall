package supportkim.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(Member member) {
        memberRepository.save(member);
        return member;
    }

    public Member findByLoginId(String loginId){
        Member findMember = memberRepository.findByLoginId(loginId);
        return findMember;
    }


}
