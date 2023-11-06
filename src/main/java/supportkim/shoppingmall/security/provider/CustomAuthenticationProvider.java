package supportkim.shoppingmall.security.provider;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import supportkim.shoppingmall.security.service.CustomMemberDetailsService;
import supportkim.shoppingmall.security.service.MemberContext;

@Component
@NoArgsConstructor
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomMemberDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("1");
        log.info("authentication : {}",authentication.getName());
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();

        MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(loginId);
        log.info("2");

        if (!passwordEncoder.matches(password,memberContext.getMember().getPassword())){
            throw new BadCredentialsException("BadCredentialException");
        }
        log.info("3");
        return new UsernamePasswordAuthenticationToken(memberContext.getMember(),null, memberContext.getAuthorities());

    }
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
