package supportkim.shoppingmall.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import supportkim.shoppingmall.domain.Kimchi;

import java.util.List;
import java.util.Optional;

public interface KimchiRepository extends JpaRepository<Kimchi , Long> {

    @Query("select k from Kimchi as k where k.type='B' ")
    public List<Kimchi> findAllBaeChu();

    @Query("select k from Kimchi as k where k.type='Y' ")
    public List<Kimchi> findAllYeolmu();

    @Query("select k from Kimchi as k where k.type='R' ")
    public List<Kimchi> findAllRadish();

    @Query("select k from Kimchi as k where k.type='GO' ")
    public List<Kimchi> findAllGreenOnion();

    Optional<Kimchi> findByName(String name);

//    public List<Kimchi> findAll();

    // Page : Count 추가 쿼리 날라감
    // 전체 페이지 수 및 전체 데이터 수가 필요하기 때문에 Count 추가 쿼리 필요
    public Page<Kimchi> findAll(Pageable pageable);





    // Slice : Count 추가 쿼리 날라가지 않음
//    public Slice<Kimchi> findAll(Pageable pageable);


}
