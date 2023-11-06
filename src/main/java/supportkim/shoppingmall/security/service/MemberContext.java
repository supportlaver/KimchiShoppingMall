package supportkim.shoppingmall.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import supportkim.shoppingmall.domain.member.Member;

import java.util.Collection;
import java.util.List;

public class MemberContext extends User {
    private final Member member;
    public MemberContext(Member member,Collection<? extends GrantedAuthority> authorities) {
        super(member.getLoginId(), member.getPassword(), authorities);
        this.member = member;
    }
    public Member getMember() {
        return member;
    }
}
