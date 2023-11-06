package supportkim.shoppingmall.security.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.service.MemberService;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@RequiredArgsConstructor
@Slf4j
public class CustomMemberDetailsService implements UserDetailsService {
    private final MemberService memberService;
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberService.findByLoginId(loginId);
        log.info("loginId : {}",loginId);

        if (member==null) {
            log.info("Current LoginId Not Found Exception");
            throw new UsernameNotFoundException("Current LoginId Not Found Exception");
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(member.getRole().toString()));
        return new MemberContext(member,roles);
    }
}
