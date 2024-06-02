package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.global.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Builder @Getter
@AllArgsConstructor @NoArgsConstructor
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
    private List<OrderKimchi> orderKimchis = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private Delivery delivery;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;

    private int orderPrice;

    public static Order of(List<OrderKimchi> orderKimchis , Member member , int orderPrice) {

        return Order.builder()
                .orderDate(LocalDateTime.now())
                .orderStatus(OrderStatus.ORDER)
                .member(member)
                .orderPrice(orderPrice)
                .build();
    }

}
