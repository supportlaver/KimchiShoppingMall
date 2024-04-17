package supportkim.shoppingmall.domain.member;

import jakarta.persistence.*;
import lombok.*;
import supportkim.shoppingmall.api.dto.MemberRequestDto;
import supportkim.shoppingmall.domain.*;
import supportkim.shoppingmall.global.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter
public class Member extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String loginId;
    private String password;
    private String name;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    // 회원이 담긴 리뷰 목록
    @OneToMany(mappedBy = "member")
    private List<Review> reviews = new ArrayList<>();

    @Embedded
    private Address address;

    private String phoneNumber;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany
    private List<Coupon> coupons;

    public void setInitCart(Cart cart) {
        this.cart = cart;
    }

    // 생성 메서드 (회원가입 전용)
    public static Member of(MemberRequestDto.SignUp signUpDto , Address address) {
        return Member.builder()
                .loginId(signUpDto.getLoginId())
                .password(signUpDto.getPassword())
                .role(Role.USER)
                .address(address)
                .phoneNumber(signUpDto.getPhoneNumber())
                .email(signUpDto.getEmail())
                .name(signUpDto.getName())
                .build();
    }

    public void setPasswordEncoder(String password) {
        this.password = password;
    }
}
