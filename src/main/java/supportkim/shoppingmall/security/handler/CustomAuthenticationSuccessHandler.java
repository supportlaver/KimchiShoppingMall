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

    private ObjectMapper om = new ObjectMapper();
    private final JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member = (Member) authentication.getPrincipal();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 영속성 컨텍스트에 대상을 만들기 위해서 직접 쿼리를 날려 영속성 컨텍스트의 대상이 되도록 만들기

        Member contextMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.SUCCESS));

        TokenMapping token = jwtService.createToken(contextMember.getEmail());
        contextMember.updateRefreshToken(token.getRefreshToken());

        Login loginMember = Login.from(contextMember,token);
        om.writeValue(response.getWriter() , loginMember);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
