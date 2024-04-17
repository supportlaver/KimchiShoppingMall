package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.global.BaseEntity;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class OrderKimchi extends BaseEntity {

    @Id @GeneratedValue
    @Column(name="order_kimchi_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kimchi_id")
    private Kimchi kimchi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    public void addCount(int count) {
        this.count += count;
    }
}
