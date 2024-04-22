package supportkim.shoppingmall.api.kimchi;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import supportkim.shoppingmall.api.dto.KimchiRequestDto;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.global.BaseResponse;
import supportkim.shoppingmall.service.KimchiService;

import static supportkim.shoppingmall.api.dto.KimchiRequestDto.*;
import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KimchiController {

    private final KimchiService kimchiService;

    // 단건 조회 API
    @GetMapping("/kimchi/{kimchi-id}")
    public ResponseEntity<BaseResponse<SingleKimchi>> getKimchi(@PathVariable("kimchi-id") Long kimchiId) {
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.findOne(kimchiId)));
    }

    // 모두 조회 API
    @GetMapping("/kimchis")
    public ResponseEntity<BaseResponse<KimchiList>> getKimchis() {
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.findAll()));
    }

    // 김치 장바구니에 담기
    @PostMapping("/cart/{kimchi-id}")
    public ResponseEntity<BaseResponse<CartKimchi>> addCart(@RequestBody KimchiCart kimchiCartDto,
                                                            @PathVariable("kimchi-id") Long kimchiId,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok().body(new BaseResponse<>(kimchiService.addCart(kimchiCartDto , kimchiId, request)));
    }
}
