package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.domain.member.Member;

import java.util.Optional;

import static supportkim.shoppingmall.api.dto.MemberResponseDto.*;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByLoginId(String loginId);
    Member findByName(String name);

    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberByRefreshToken(String refreshToken);

}
