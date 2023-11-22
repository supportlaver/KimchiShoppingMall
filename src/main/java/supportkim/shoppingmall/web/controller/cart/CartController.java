package supportkim.shoppingmall.web.controller.cart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import supportkim.shoppingmall.domain.Cart;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.domain.OrderKimchi;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.repository.CartRepository;
import supportkim.shoppingmall.repository.KimchiRepository;
import supportkim.shoppingmall.repository.OrderKimchiRepository;
import supportkim.shoppingmall.service.CartService;
import supportkim.shoppingmall.service.MemberService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final MemberService memberService;
    private final KimchiRepository kimchiRepository;
    private final OrderKimchiRepository orderKimchiRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;

    @GetMapping("/cart")
    public String cart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        Member member = memberService.findByLoginId(name);

        List<OrderKimchi> memberCart = member.getCart().getCartItems();
        int subTotal = 0;
        for (OrderKimchi orderKimchi : memberCart) {
            subTotal+=orderKimchi.getOrderPrice();
        }
        model.addAttribute("memberCart", memberCart);
        model.addAttribute("subTotal",subTotal);
        return "cart/cart";
    }

    @PostMapping("/add-cart/{kimchi}")
    public String addCart(@PathVariable("kimchi") Long id, @RequestParam(value = "count", required = false) int count) {

        // 7 로 넣어도 왜 6 으로 들어가지..? 그래서 일단 +1 해주기
        count += 1;

        // 카트에 넣으려고 하는 Member 찾기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Member findMember = memberService.findByLoginId(loginId);

        // 주문할 김치 찾기
        Kimchi kimchi = kimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        // orderKimchi 로 만들고 cart 에 있는 List<OrderKimchi> 에다가 추가하기
        // 이것도 컨트롤러에 필요한 로직은 아니잖아. 그래서 이것도 OrderKimchi 에다가 해서 최적화 하는게 맞긴해 이것도 하기
        OrderKimchi orderKimchi = OrderKimchi.builder()
                .count(count)
                .orderPrice((kimchi.getPrice() * count))
                .kimchi(kimchi)
                .cart(findMember.getCart())
                .build();

        orderKimchiRepository.save(orderKimchi);

        // 카트가 비어있다면 카트 객체를 새로 생성해서 반환하고
        // 원래 카트가 있다면 기존의 카트를 반환한다.

        cartService.addCart(findMember, orderKimchi);

        // 해보고 장바구니 담기 성공 이라는 페이지를 넣고 이전 페이지로 가게 할지 장바구니로 가게 할지 2개 버튼 주는걸로 해보자.
        List<OrderKimchi> cartItems = findMember.getCart().getCartItems();
        return "redirect:/";
    }

    @PostMapping("/delete-cart-{orderKimchi}")
    public String delete(@PathVariable("orderKimchi") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        Member findMember = memberService.findByLoginId(loginId);
        List<OrderKimchi> cartItems = findMember.getCart().getCartItems();
        OrderKimchi orderKimchi = orderKimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);

        // orderKimchi 취소
        orderKimchiRepository.delete(orderKimchi);
        cartItems.remove(orderKimchi);

        // Cart DB 에도 없애기
        cartService.cancelCart(findMember , orderKimchi);

        return "redirect:/cart";
    }
}