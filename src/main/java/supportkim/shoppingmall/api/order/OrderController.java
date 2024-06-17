package supportkim.shoppingmall.api.order;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import supportkim.shoppingmall.api.dto.OrderRequestDto;
import supportkim.shoppingmall.api.dto.OrderResponseDto;
import supportkim.shoppingmall.global.BaseResponse;
import supportkim.shoppingmall.service.OrderService;

import java.nio.file.Path;

import static supportkim.shoppingmall.api.dto.OrderResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    // 장바구니에서 주문하는 API
    @PostMapping
    public ResponseEntity<BaseResponse<CompleteOrder>> cartOrder(HttpServletRequest request) {
        return ResponseEntity.ok().body(new BaseResponse<>(orderService.cartOrder(request)));
    }

    // 상품 조회에서 바로 주문하는 API
    @PostMapping("/{kimchi-id}")
    public ResponseEntity<BaseResponse<CompleteOrder>> setOrder(@PathVariable("kimchi-id") Long id,
                                                                @RequestBody OrderRequestDto.SingleOrder singleOrder) {
        return ResponseEntity.ok().body(new BaseResponse<>(orderService.setOrder(id,singleOrder)));
    }




    // 주문할 때 쿠폰 적용 (현재 모든 회원들에게 발급된 쿠폰은 "회원가입 쿠폰" 만 존재
    // RestAPI 만 존재하기 떄문에 order-id 를 따로 받고 쿠폰 적용
    @PostMapping("/coupon/{order-id}")
    public ResponseEntity<BaseResponse<OrderResponseDto.ApplyCouponOrder>> applyCoupon(HttpServletRequest request,
                                                             @PathVariable("order-id") Long orderId) {
        return ResponseEntity.ok().body(new BaseResponse<>(orderService.applyCoupon(request, orderId)));
    }

    // 주문 취소 API
    @PostMapping("/cancel/{order-id}")
    public ResponseEntity<BaseResponse<String>> orderCancel(@PathVariable("order-id") Long orderId) {
        return ResponseEntity.ok().body(new BaseResponse<>(orderService.cancel(orderId)));
    }
}
