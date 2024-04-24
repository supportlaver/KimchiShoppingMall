package supportkim.shoppingmall.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import supportkim.shoppingmall.api.dto.CouponResponseDto;
import supportkim.shoppingmall.domain.Coupon;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.CouponRepository;
import supportkim.shoppingmall.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static supportkim.shoppingmall.api.dto.CouponResponseDto.*;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public CouponList getCouponList(HttpServletRequest request) {

        Member member = findMemberFromAccessToken(request);
        List<SingleCoupon> coupons = couponRepository.findAllByMemberId(member.getId())
                .stream().map(SingleCoupon::new)
                .collect(toList());
        return CouponList.of(coupons);

    }

    private Member findMemberFromAccessToken(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ACCESS_TOKEN));
        String email = jwtService.extractMemberEmail(accessToken);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
    }
}
