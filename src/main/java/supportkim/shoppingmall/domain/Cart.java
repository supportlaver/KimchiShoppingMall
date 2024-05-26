package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.global.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Cart extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    // 카트에 담긴 총 상품 개수
    private int count;

    @OneToMany(mappedBy = "cart")
    private List<OrderKimchi> cartItems = new ArrayList<>();

    public void addCount(int count) {
        this.count += count;
    }

    public void setInitMember(Member member) {
        this.member = member;
    }

    public void cancelCount(int count) {
        this.count -= count;
    }

    // 생성 메서드 (회원가입 할 때 카트 할당)
    public static Cart from() {
        return Cart.builder()
                .build();
    }

    // 연관관계 편의 메서드
    public void initSingUp(Member member) {
        this.member = member;
        member.setInitCart(this);
    }

    public void forTestInitKimchi(List<OrderKimchi> cartItems) {
        this.cartItems = cartItems;
    }
}
