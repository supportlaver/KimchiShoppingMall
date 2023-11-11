package supportkim.shoppingmall.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String name;

    private int price;

//    private List<String> imagesURL = new ArrayList<>();

    // 해당 김치에 대한 리뷰들
    @OneToMany(mappedBy = "kimchi")
    private List<Review> reviews = new ArrayList<>();

}
