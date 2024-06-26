package supportkim.shoppingmall.service;

import io.micrometer.core.annotation.Counted;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.api.dto.OrderRequestDto;
import supportkim.shoppingmall.api.dto.OrderResponseDto;
import supportkim.shoppingmall.consumer.AlarmConsumer;
import supportkim.shoppingmall.domain.*;
import supportkim.shoppingmall.domain.alarm.AlarmEvent;
import supportkim.shoppingmall.domain.alarm.AlarmType;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.producer.AlarmProducer;
import supportkim.shoppingmall.repository.*;
import supportkim.shoppingmall.utils.ClassUtils;

import java.util.List;
import java.util.Optional;

import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;
import static supportkim.shoppingmall.api.dto.OrderResponseDto.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final AlarmService alarmService;
    private final CouponRepository couponRepository;
    private final KimchiRepository kimchiRepository;
    private final AlarmProducer alarmProducer;
    private final JwtService jwtService;
    private int COUNT = 0;

    @Transactional
    @Counted("indicator.order")
    public CompleteOrder cartOrder(HttpServletRequest request) {

        log.info("[함수 시작 직후] 김치 수량 확인 : " + kimchiRepository.findById(1L).orElseThrow().getQuantity());

        COUNT+=1;
        log.info("현재 주문 횟수 : " + COUNT);

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

        alarmService.send(AlarmType.COMPLETE_ORDER,member.getId());

        log.info("[함수 끝나기 직전] 김치 수량 확인 : " + kimchiRepository.findById(1L).orElseThrow().getQuantity());

        return CompleteOrder.of(savedOrder,orderPrice);
    }

    @Transactional
    @Counted("indicator.order")
    public CompleteOrder setOrder(Long id , OrderRequestDto.SingleOrder singleOrder) {

        int orderPrice = 0;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Member member = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Member.class);

        /**
         * Filter 에서 이미 확인 후 Cache 해놓기 때문에 또 검증할 필요가 없다.
         */
        // Member member = findMemberFromAccessToken(request);

        Kimchi kimchi = kimchiRepository.findById(id)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_KIMCHI));


        OrderKimchi orderKimchi = OrderKimchi.createEntityByKimchi(kimchi , singleOrder.getCount());
        /**
         * 수량 감소
         */
        orderPrice += orderKimchi.getOrderPrice();
        orderKimchi.decreaseQuantity(orderKimchi.getCount());

        Order order = Order.ofOneKimchi(member , orderPrice);

        Order savedOrder = orderRepository.save(order);

        /**
         * Kafka 에 던지기
         */
        alarmProducer.send(new AlarmEvent(AlarmType.COMPLETE_ORDER , member.getId()));
        // alarmService.send(AlarmType.COMPLETE_ORDER,member.getId());

        return CompleteOrder.of(savedOrder,orderPrice);
    }

    @Transactional
    public CompleteOrder cartOrderNoRequest(Member member) {
        int orderPrice = 0;

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
    @Counted("indicator.cancel")
    public String cancel(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ORDER));
        // 수량 다시 복구 시키기
        List<OrderKimchi> orderKimchis = order.getOrderKimchis();
        for (OrderKimchi orderKimchi : orderKimchis) {
            orderKimchi.increaseQuantity(orderKimchi.getCount());
        }
        orderRepository.delete(order);
        return "주문 취소 완료";
    }


    @Transactional
    public OrderResponseDto.ApplyCouponOrder applyCoupon(HttpServletRequest request , Long orderId) {
        Member member = findMemberFromAccessToken(request);
        List<Coupon> coupon = member.getCoupons();

        //todo 현재 회원가입 쿠폰만 존재하기 때문에 이런 로직으로 작성하지만 확장 되면 변경해야 합니다.
        Coupon signUpCoupon = coupon.get(1);


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
