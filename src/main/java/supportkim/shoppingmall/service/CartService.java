package supportkim.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.domain.Cart;
import supportkim.shoppingmall.domain.Order;
import supportkim.shoppingmall.domain.OrderKimchi;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.repository.CartRepository;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public void save(Cart cart){
        cartRepository.save(cart);
    }


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
