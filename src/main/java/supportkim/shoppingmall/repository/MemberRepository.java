package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supportkim.shoppingmall.domain.member.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByLoginId(String loginId);
    Member findByName(String name);

    Optional<Member> findByEmail(String email);

    Optional<Member> findMemberByRefreshToken(String refreshToken);
}
