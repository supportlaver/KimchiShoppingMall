package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import supportkim.shoppingmall.domain.Coupon;

public interface LockRepository extends JpaRepository<Coupon , Long> {

    @Query(value = "select get_lock(:key , 100)" , nativeQuery = true)
    void getLock(@Param("key") String key);

    @Query(value = "select release_lock(:key)" , nativeQuery = true)
    void releaseLock(@Param("key") String key);

}
