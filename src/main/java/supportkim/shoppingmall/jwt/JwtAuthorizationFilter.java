package supportkim.shoppingmall.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import supportkim.shoppingmall.api.dto.MemberResponseDto;
import supportkim.shoppingmall.api.member.MemberController;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.repository.MemberCacheRepository;
import supportkim.shoppingmall.repository.MemberRepository;
import supportkim.shoppingmall.security.token.CustomAuthenticationToken;

import java.io.IOException;
import java.util.Optional;

import static supportkim.shoppingmall.api.dto.MemberResponseDto.*;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberCacheRepository memberCacheRepository;

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


        Member member = null;

        /**
         * 캐시에서 먼저 확인 후 없으면 DB 에서 가져오기
         * 로그인 할 때 처음에는 캐시에 없기 떄문에 DB 에서 가지고 온다.
         */


        Optional<SingleMember> findMember = memberCacheRepository.getMember(email);
        if (findMember.isEmpty()) {
            member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));

        } else {
            SingleMember singleMember = findMember.get();
            member = SingleMember.fromMemberEntity(singleMember.getMemberId(), singleMember.getName(), singleMember.getName());
        }

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

