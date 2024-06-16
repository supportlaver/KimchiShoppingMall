package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.domain.alarm.Alarm;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm , Long> {
    @Query("select a from Alarm as a where a.member.id = :memberId")
    public List<Alarm> findAllByMemberId(Long memberId);
}
