package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.api.dto.KimchiRequestDto;
import supportkim.shoppingmall.domain.member.Member;
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

    public static OrderKimchi of(KimchiRequestDto.KimchiCart kimchiCartDto, Kimchi kimchi , Member member) {
        return OrderKimchi.builder()
                .count(kimchiCartDto.getCount())
                .orderPrice((kimchi.getPrice() * kimchiCartDto.getCount()))
                .kimchi(kimchi)
                .cart(member.getCart())
                .build();
    }

    public static OrderKimchi createEntityByKimchi(Kimchi kimchi , int count) {
        return OrderKimchi.builder()
                .count(count)
                .orderPrice((kimchi.getPrice()) * count)
                .kimchi(kimchi)
                .build();
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void decreaseQuantity(int count) {
        Kimchi kimchi = this.getKimchi();
        kimchi.decreaseQuantity(count);
    }

    public void increaseQuantity(int count) {
        Kimchi kimchi = this.getKimchi();
        kimchi.increaseQuantity(count);
    }

    public void forTestInitKimchi(Kimchi kimchi) {
        this.kimchi = kimchi;
    }


}
