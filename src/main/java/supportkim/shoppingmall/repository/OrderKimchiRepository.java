package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supportkim.shoppingmall.domain.OrderKimchi;

public interface OrderKimchiRepository extends JpaRepository<OrderKimchi , Long> {
}
