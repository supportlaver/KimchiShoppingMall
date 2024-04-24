package supportkim.shoppingmall.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.OrderResponseDto;
import supportkim.shoppingmall.domain.*;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.CouponRepository;
import supportkim.shoppingmall.repository.MemberRepository;
import supportkim.shoppingmall.repository.OrderRepository;

import java.util.Iterator;
import java.util.List;

import static supportkim.shoppingmall.api.dto.OrderResponseDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final JwtService jwtService;

    @Transactional
    public CompleteOrder order(HttpServletRequest request) {
        int orderPrice = 0;

        Member member = findMemberFromAccessToken(request);

        Cart cart = member.getCart();
        List<OrderKimchi> orderKimchis = cart.getCartItems();

        if (orderKimchis.size() == 0) {
            throw new BaseException(ErrorCode.EMPTY_CART);
        }

        // 수량 감소 시키기
        for (OrderKimchi orderKimchi : orderKimchis) {
            orderPrice += orderKimchi.getOrderPrice();
            orderKimchi.decreaseQuantity(orderKimchi.getCount());
        }

        Order order = Order.of(orderKimchis, member , orderPrice);

        Order savedOrder = orderRepository.save(order);

        return CompleteOrder.of(savedOrder,orderPrice);
    }

    @Transactional
    public OrderResponseDto.ApplyCouponOrder applyCoupon(HttpServletRequest request , Long orderId) {
        Member member = findMemberFromAccessToken(request);
        List<Coupon> coupon = couponRepository.findAllByMemberId(member.getId());

        //todo 현재 회원가입 쿠폰만 존재하기 때문에 이런 로직으로 작성하지만 확장 되면 변경해야 합니다.
        Coupon signUpCoupon = coupon.get(0);


        if (signUpCoupon.getCouponStatus() == CouponStatus.beforeUSED) {
            throw new BaseException(ErrorCode.ALREADY_USED_COUPON);
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ORDER));

        signUpCoupon.usedCoupon();
        Integer discountValue = signUpCoupon.getDiscountValue();
        Integer orderPrice = order.getOrderPrice();

        Integer discountPrice = orderPrice * discountValue / 100;
        Integer applyCouponPrice = orderPrice - discountPrice;

        return OrderResponseDto.ApplyCouponOrder.of(discountPrice,applyCouponPrice);
    }

    private Member findMemberFromAccessToken(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ACCESS_TOKEN));
        String email = jwtService.extractMemberEmail(accessToken);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
    }

}
