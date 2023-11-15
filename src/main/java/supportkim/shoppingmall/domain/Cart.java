package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.*;
import supportkim.shoppingmall.domain.member.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor @AllArgsConstructor
public class Cart {

    @Id @GeneratedValue
    @Column(name="cart_id")
    private Long id;

    @OneToOne
    private Member member;

    @OneToMany
    private List<OrderKimchi> orderKimchis = new ArrayList<>();

    private int count;
    private int price;






}
