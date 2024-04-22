package supportkim.shoppingmall.api.order;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import supportkim.shoppingmall.api.dto.OrderRequestDto;
import supportkim.shoppingmall.api.dto.OrderResponseDto;
import supportkim.shoppingmall.domain.OrderStatus;
import supportkim.shoppingmall.global.BaseResponse;
import supportkim.shoppingmall.service.OrderService;

import static supportkim.shoppingmall.api.dto.OrderResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    // 장바구니에서 주문하는 API
    @PostMapping("/order")
    public ResponseEntity<BaseResponse<CompleteOrder>> order(HttpServletRequest request) {
        log.info("OrderController");
        return ResponseEntity.ok().body(new BaseResponse<>(orderService.order(request)));

    }
}
