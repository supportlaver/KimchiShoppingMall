package supportkim.shoppingmall.domain.alarm;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.web.service.annotation.GetExchange;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.global.BaseEntity;

@Entity @Builder
@AllArgsConstructor
@Getter @NoArgsConstructor
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;



    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    public static Alarm of(AlarmType type ,Member member) {
        return Alarm.builder()
                .alarmType(type)
                .member(member)
                .build();
    }
}
