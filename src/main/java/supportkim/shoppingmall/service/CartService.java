package supportkim.shoppingmall.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.CartResponseDto;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.domain.Cart;
import supportkim.shoppingmall.domain.Order;
import supportkim.shoppingmall.domain.OrderKimchi;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.CartRepository;
import supportkim.shoppingmall.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

import static supportkim.shoppingmall.api.dto.CartResponseDto.*;
import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public void save(Cart cart){
        cartRepository.save(cart);
    }

    public getCart getCart(HttpServletRequest request) {
        Member member = findMemberFromAccessToken(request);

        List<OrderKimchi> cartItems = member.getCart().getCartItems();
        List<SingleKimchi> result = cartItems.stream()
                .map(ok -> new SingleKimchi(ok.getKimchi().getId() , ok.getKimchi().getName()))
                .toList();

        return getCart.from(result);
    }

    private Member findMemberFromAccessToken(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ACCESS_TOKEN));
        String email = jwtService.extractMemberEmail(accessToken);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
    }





    /**
     * 이전 코드
     */
    @Transactional
    public void addCart(Member findMember, OrderKimchi orderKimchi) {
        Cart cart = findMember.getCart();
        cart.getCartItems().add(orderKimchi);
        cart.addCount(orderKimchi.getCount());
    }

    @Transactional
    public void cancelCart(Member findMember, OrderKimchi orderKimchi) {
        Cart cart = findMember.getCart();
        cart.getCartItems().remove(orderKimchi);
        cart.cancelCount(orderKimchi.getCount());
    }

}
