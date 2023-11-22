package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Set;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Kimchi {

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

    // 해당 김치에 대한 리뷰들
    @OneToMany(mappedBy = "kimchi")
    private List<Review> reviews = new ArrayList<>();
}
