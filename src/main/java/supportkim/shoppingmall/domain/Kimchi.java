package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.global.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Kimchi extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "kimchi_id")
    private Long id;

    // 김치에 대한 요약(간략한 정보)
    private String summaryInfo;

    private String imageURL;
    private String moreInfoImageURL;

    private String name;

    @Enumerated(EnumType.STRING)
    private KimchiType type;

    private int price;

    private int quantity;

    // 해당 김치에 대한 리뷰들
    @OneToMany(mappedBy = "kimchi")
    private List<Review> reviews = new ArrayList<>();


    // 수량 관리 (감소)
    public void decreaseQuantity(int count) {
        if (quantity-count<0) {
            throw new BaseException(ErrorCode.OVER_QUANTITY);
        }
        this.quantity = quantity - count;
    }
}
