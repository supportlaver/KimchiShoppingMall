package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import supportkim.shoppingmall.domain.Coupon;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon , Long> {

    List<Coupon> findAllByMemberId(Long memberId);
}
