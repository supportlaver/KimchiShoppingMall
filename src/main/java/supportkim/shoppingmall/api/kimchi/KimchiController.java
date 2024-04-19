package supportkim.shoppingmall.api.kimchi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.global.BaseResponse;
import supportkim.shoppingmall.service.KimchiService;

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
}
