package supportkim.shoppingmall.domain.member;

import jakarta.persistence.*;
import lombok.*;
import supportkim.shoppingmall.domain.*;

import java.util.ArrayList;
import java.util.List;

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

    @OneToOne
    private Cart cart;

    @OneToMany
    private List<Coupon> coupons;

    public void setInitCart(Cart cart) {
        this.cart = cart;
    }
}
