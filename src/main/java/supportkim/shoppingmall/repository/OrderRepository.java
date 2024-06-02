package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supportkim.shoppingmall.domain.Order;

public interface OrderRepository extends JpaRepository<Order , Long> {
}
