package supportkim.shoppingmall.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import supportkim.shoppingmall.api.member.MemberController;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.repository.MemberRepository;
import supportkim.shoppingmall.security.token.CustomAuthenticationToken;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;

    public JwtAuthorizationFilter(JwtService jwtService, MemberRepository memberRepository) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresentOrElse(
                        this::saveAuthentication,
                        () -> checkRefreshToken(request,response)
                );
        filterChain.doFilter(request, response);
    }

    /**
     * accessToken 유효 -> authentication 저장
     * accessToken 만료
     *      refreshToken 유효 -> authentication 저장, accessToken 갱신
     *      refreshToken 만료 -> authentication 저장 X
     */

    private void saveAuthentication(String accessToken) {
        String email = jwtService.extractMemberEmail(accessToken);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
        CustomAuthenticationToken token = new CustomAuthenticationToken(member, null);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private void checkRefreshToken(HttpServletRequest request , HttpServletResponse response) {
        Optional<String> refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid);

        if (refreshToken.isPresent()) {
            Member member = memberRepository.findMemberByRefreshToken(refreshToken.get())
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
            String accessToken = jwtService.createAccessToken(member.getEmail());
            jwtService.setAccessTokenInHeader(response , accessToken);
            saveAuthentication(accessToken);
        } else {
            doNotSaveAuthentication(request , response);
        }
    }

    private void doNotSaveAuthentication(HttpServletRequest request , HttpServletResponse response)  {

    }
}

