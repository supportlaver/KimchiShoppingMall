package supportkim.shoppingmall.api.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.repository.KimchiCacheRepository;
import supportkim.shoppingmall.repository.MemberCacheRepository;

import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class RedisCacheController {

    private final KimchiCacheRepository redisCacheKimchiRepository;
    private final MemberCacheRepository memberCacheRepository;

    // 캐시 API
    @PostMapping("/cache-kimchi")
    public String cacheKimchi(@RequestBody SingleKimchi kimchi) {
        redisCacheKimchiRepository.setKimchi(kimchi);

        // 확인
        String key = redisCacheKimchiRepository.getKey(kimchi.getName());
        return "캐시 등록된 key : " + key;
    }

    @DeleteMapping("/cache-kimchi")
    public String deleteCacheKimchi(@RequestBody String name) {
        redisCacheKimchiRepository.deleteKimchi(name);
        return name + "-> 삭제 완료";
    }

    @DeleteMapping("/cache-member")
    public String deleteCacheMember(@RequestBody String email) {
        memberCacheRepository.deleteMember(email);
        return email + " -> 삭제 완료";
    }
}
