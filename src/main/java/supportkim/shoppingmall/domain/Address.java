package supportkim.shoppingmall.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.api.dto.MemberRequestDto;

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

    // 생성 메서드 (회원가입 전용)
    public static Address of(MemberRequestDto.SignUp signUpDto) {
        return Address.builder()
                .zipCode(signUpDto.getZipCode())
                .reference(signUpDto.getReference())
                .moreInfo(signUpDto.getMoreInfo())
                .streetCode(signUpDto.getStreetCode())
                .build();
    }
}
