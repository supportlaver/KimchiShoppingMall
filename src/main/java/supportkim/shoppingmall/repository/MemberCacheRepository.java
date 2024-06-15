package supportkim.shoppingmall.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import supportkim.shoppingmall.api.dto.MemberResponseDto;

import javax.swing.text.html.Option;

import java.util.Optional;

import static supportkim.shoppingmall.api.dto.MemberResponseDto.*;

@RequiredArgsConstructor
@Repository
public class MemberCacheRepository {

    private final RedisTemplate<String , SingleMember> memberRedisTemplate;

    public void setMember(SingleMember member) {
        memberRedisTemplate.opsForValue().set(getKey(member.getEmail()) , member);
    }

    public Optional<SingleMember> getMember(String email) {
        SingleMember member = memberRedisTemplate.opsForValue().get(getKey(email));
        return Optional.ofNullable(member);
    }

    public void deleteMember(String email) {
        memberRedisTemplate.delete(getKey(email));
    }

    public String getKey (String email) {
        return "MEMBER:" + email;
    }
}
