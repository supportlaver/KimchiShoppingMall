package supportkim.shoppingmall.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Address {

    // 우편번호 (21640)
    private String zipCode;

    // 도로명 주소 (은봉로 154번길 24)
    private String streetCode;

    // 상세주소 (101동 101호)
    private String moreInfo;

    // 참고사항
    private String reference;




}
