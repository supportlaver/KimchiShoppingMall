package supportkim.shoppingmall.service;

import io.micrometer.core.annotation.Counted;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.KimchiRequestDto;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.domain.OrderKimchi;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.KimchiRepository;
import supportkim.shoppingmall.repository.MemberRepository;
import supportkim.shoppingmall.repository.OrderKimchiRepository;

import java.util.List;
import java.util.Optional;

import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class KimchiService {

    private final KimchiRepository kimchiRepository;
    private final MemberRepository memberRepository;
    private final OrderKimchiRepository orderKimchiRepository;
    private final JwtService jwtService;

    // 단건 조회
    @Counted("indicator.kimchi")
    public SingleKimchi findOneByPK(Long kimchiId) {
        Kimchi kimchi = kimchiRepository.findById(kimchiId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_KIMCHI));
        
        return SingleKimchi.from(kimchi);
    }

    @Counted("indicator.kimchi.name")
    public SingleKimchi findOneByName(String kimchiName) {
        Kimchi kimchi = kimchiRepository.findByName(kimchiName)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_KIMCHI));

        return SingleKimchi.from(kimchi);
    }

    // 모두 조회 (페이징 추가)
    public KimchiList findAll(Pageable pageable) {
//        List<Kimchi> findKimchiList = kimchiRepository.findAll();
        // PageRequest (현재 페이지 , 조회할 데이터 수 , 정렬 정보) : 페이지 0 부터 시작

        Page<Kimchi> findKimchiList = kimchiRepository.findAll(pageable);

        /**
         * 순서대로
         * 다음 페이지가 있는가?
         * 첫 번째 페이지인가?
         * 전체 Page 계산
         * page 번호 계산
         */
//        findKimchiList.hasNext();
//        findKimchiList.isFirst();
//        findKimchiList.getTotalPages();
//        findKimchiList.getNumber();
        log.info("다음 페이지가 있는가 ? = {} " , findKimchiList.hasNext());
        log.info("첫 번째 페이지 인가 ? = {} " , findKimchiList.isFirst());
        log.info("전체 페이지는 ? = {} " , findKimchiList.getTotalPages());
        log.info("Page 번호는 ? = {} " , findKimchiList.getNumber());


        List<SingleKimchi> result = findKimchiList.stream().map(SingleKimchi::from)
                .toList();
        return KimchiList.from(result);
    }

    @Transactional
    public CartKimchi addCart(KimchiRequestDto.KimchiCart kimchiCartDto, Long kimchiId , HttpServletRequest request) {



        Member member = findMemberFromAccessToken(request);
        log.info("현재 {} 가 장바구니에 김치를 담았습니다." , member.getName());

        Kimchi kimchi = kimchiRepository.findById(kimchiId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_KIMCHI));

        // OrderKimchi 생성
        OrderKimchi orderKimchi = OrderKimchi.of(kimchiCartDto, kimchi, member);
        orderKimchiRepository.save(orderKimchi);

        // 해당 Member 의 장바구니에 생성한 OrderKimchi 추가
        member.getCart().getCartItems().add(orderKimchi);

        return CartKimchi.from(orderKimchi);
    }

    /**
     * 모니터링을 위한 재고 조회
     */

    public Integer getStock() {
        //todo 이벤트하는 상품들의 PK 값을 미리 정해놓고 여기에 값을 계산해서 반환하는 로직이 필요합니다.
        // 현재는 1번이라고 가정
        Optional<Kimchi> findKimchi = kimchiRepository.findById(1L);
        return findKimchi.get().getQuantity();
    }

    private Member findMemberFromAccessToken(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_REFRESH_TOKEN));
        String email = jwtService.extractMemberEmail(accessToken);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
    }
}
