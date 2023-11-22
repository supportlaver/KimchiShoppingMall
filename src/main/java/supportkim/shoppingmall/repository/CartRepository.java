package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supportkim.shoppingmall.domain.Cart;

public interface CartRepository extends JpaRepository<Cart , Long> {
}
