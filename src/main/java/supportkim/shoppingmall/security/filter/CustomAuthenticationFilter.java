package supportkim.shoppingmall.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import supportkim.shoppingmall.api.dto.MemberRequestDto;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.MemberRepository;
import supportkim.shoppingmall.security.token.CustomAuthenticationToken;

import java.io.IOException;
import java.util.Optional;

import static supportkim.shoppingmall.api.dto.MemberRequestDto.*;

@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private ObjectMapper om = new ObjectMapper();

    public CustomAuthenticationFilter() {
        // /api/login 과 일치할 경우 해당 필터가 동작한다.
        super(new AntPathRequestMatcher("/api/login"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        // 요청 방식이 POST 인지 확인
        if (!isPost(request)) {
            throw new BaseException(ErrorCode.NOT_SUPPORT_METHOD);
        }


        Login loginDto = om.readValue(request.getReader(), Login.class);

        // loginId 와 password 가 포함되어 있는지 확인
        if (!StringUtils.hasLength(loginDto.getLoginId()) || !StringUtils.hasLength(loginDto.getPassword())) {
            throw new BaseException(ErrorCode.EMPTY_LOGIN_INFO);
        }


        CustomAuthenticationToken token = new CustomAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());


        // Manager 에게 인증처리 위임
        Authentication authenticate = getAuthenticationManager().authenticate(token);

        return authenticate;
    }

    private boolean isPost(HttpServletRequest request) {
        if ("POST".equals(request.getMethod())) {
            return true;
        }
        return false;
    }
}