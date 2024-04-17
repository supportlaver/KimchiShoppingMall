package supportkim.shoppingmall.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.jwt.TokenMapping;
import supportkim.shoppingmall.repository.MemberRepository;
import supportkim.shoppingmall.security.service.MemberContext;
import supportkim.shoppingmall.security.token.CustomAuthenticationToken;

import java.io.IOException;
import java.util.Optional;

import static supportkim.shoppingmall.api.dto.MemberResponseDto.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {


    private final JwtService jwtService;

    private final MemberRepository memberRepository;

    private final EntityManager em;
    private ObjectMapper om = new ObjectMapper();
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member = (Member) authentication.getPrincipal();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        TokenMapping token = getToken(member);
        member.updateRefreshToken(token.getRefreshToken());
        Login loginMember = Login.from(member,token);
        om.writeValue(response.getWriter() , loginMember);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    /**
     * 로그인에 성공한 Member 에게 JWT 발급
     */
    private TokenMapping getToken(Member member) {
        String email = member.getEmail();
        TokenMapping token = jwtService.createToken(email);
        return token;
    }
}
