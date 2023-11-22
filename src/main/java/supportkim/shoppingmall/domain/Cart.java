package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class Cart {

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
}
