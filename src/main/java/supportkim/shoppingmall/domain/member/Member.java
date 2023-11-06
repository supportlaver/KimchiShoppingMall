package supportkim.shoppingmall.domain.member;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String loginId;
    private String password;
    private String name;

    @Embedded
    private Address address;

    private String phoneNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
}
