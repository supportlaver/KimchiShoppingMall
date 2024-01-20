package supportkim.shoppingmall.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class CouponBasedEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private LocalDateTime endAt;

    // 지급 날짜?
    private LocalDateTime assignedAt;

}
