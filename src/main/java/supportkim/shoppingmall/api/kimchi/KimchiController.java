package supportkim.shoppingmall.api.kimchi;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supportkim.shoppingmall.api.dto.KimchiRequestDto;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.global.BaseResponse;
import supportkim.shoppingmall.service.CouponService;
import supportkim.shoppingmall.service.KimchiService;

import static supportkim.shoppingmall.api.dto.KimchiRequestDto.*;
import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class KimchiController {

    private final KimchiService kimchiService;
    private final CouponService couponService;

    // 단건 조회 API by PK
    @GetMapping("/kimchi/{kimchi-id}")
    public ResponseEntity<BaseResponse<SingleKimchi>> getKimchiByPK(@PathVariable("kimchi-id") Long kimchiId) {
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.findOneByPK(kimchiId)));
    }

    // 단건 조회 API by Name
    @GetMapping("/kimchi")
    public ResponseEntity<BaseResponse<SingleKimchi>> getKimchiByKimchiName(@RequestParam("kimchi-name") String kimchiName) {
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.findOneByNameWithCache(kimchiName)));
    }

    // 모두 조회 API
    // 요청 파라미터

    /**
     * 요청 파라미터
     * page : 현재 페이지 (0부터 시작)
     * size : 한 페이지에 노출할 데이터 건수
     * sort : 정렬 조건 (asc 일 경우 생략)
     * 입력이 잘 못 된 경우는 디폴트 값으로 페이징 해온다.
     * ex) /kimchis?page=0&size=4&sort=id,desc
     */
    @GetMapping("/kimchis")
    public ResponseEntity<BaseResponse<KimchiList>> getKimchis(Pageable pageable) {
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.findAll(pageable)));
    }

    // 김치 장바구니에 담기
    @PostMapping("/cart/{kimchi-id}")
    public ResponseEntity<BaseResponse<CartKimchi>> addCart(@RequestBody KimchiCart kimchiCartDto,
                                                            @PathVariable("kimchi-id") Long kimchiId,
                                                            HttpServletRequest request) {
        log.info("장바구니 담는 API 호출");
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.addCart(kimchiCartDto , kimchiId, request)));
    }

    // 수량 확인 API
    @GetMapping("/kimchi-quantity/1")
    public ResponseEntity<BaseResponse<Integer>> getKimchiQuantity() {
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.getStock()));
    }

    // 쿠폰 수량 확인 API (테스트용)
    @GetMapping("/coupon-quantity/1")
    public ResponseEntity<BaseResponse<Integer>> getCouponQuantity() {
        return ResponseEntity.ok().body(new BaseResponse<>(couponService.getCouponStock()));
    }
}
