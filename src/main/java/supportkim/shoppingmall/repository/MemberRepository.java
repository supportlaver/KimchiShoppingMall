package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supportkim.shoppingmall.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByLoginId(String loginId);
    Member findByName(String name);
}
