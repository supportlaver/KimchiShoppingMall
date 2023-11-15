package supportkim.shoppingmall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import supportkim.shoppingmall.domain.Kimchi;

import java.util.List;

public interface KimchiRepository extends JpaRepository<Kimchi , Long> {

    @Query("select k from Kimchi as k where k.type='B' ")
    public List<Kimchi> findAllBaeChu();
}
