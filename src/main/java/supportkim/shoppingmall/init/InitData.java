package supportkim.shoppingmall.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.domain.Cart;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.domain.KimchiType;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.domain.member.Role;
import supportkim.shoppingmall.repository.CartRepository;
import supportkim.shoppingmall.repository.KimchiRepository;
import supportkim.shoppingmall.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class InitData {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.baeChuInitData();
        initService.yeolMuInitData();
        initService.greenOnionInitData();
        initService.radishInitData();
        initService.memberInitData();
        initService.radishSubInitData();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService {

        private final KimchiRepository kimchiRepository;
        private final MemberRepository memberRepository;
        private final CartRepository cartRepository;
        private final PasswordEncoder passwordEncoder;

        public void baeChuInitData() {
            Kimchi baeChu1 = Kimchi.builder()
                    .name("배추김치 1kg")
                    .price(15000)
                    .type(KimchiType.B)
                    .summaryInfo("모든 재료 국내산으로 만든 엄마표 배추김치!")
                    .imageURL("/kimchi/baechu/baechu.jpeg")
                    .moreInfoImageURL("/kimchi/baechu/baechu-more-info.jpeg")
                    .build();
            kimchiRepository.save(baeChu1);
        }

        public void yeolMuInitData() {
            Kimchi yeolMu1 = Kimchi.builder()
                    .name("열무김치 1kg")
                    .price(14000)
                    .type(KimchiType.Y)
                    .summaryInfo("모든 재료 국내산으로 만든 엄마표 열무김치!")
                    .imageURL("/kimchi/yeolmu/yeolmu.jpeg")
                    .moreInfoImageURL("/kimchi/yeolmu/yeolmu-more-info.jpeg")
                    .build();
            kimchiRepository.save(yeolMu1);
        }

        public void greenOnionInitData() {
            Kimchi greenOnion1 = Kimchi.builder()
                    .name("파김치 1kg")
                    .price(17000)
                    .type(KimchiType.GO)
                    .summaryInfo("모든 재료 국내산으로 만든 엄마표 파김치!")
                    .imageURL("/kimchi/green-onion/green-onion.png")
                    .moreInfoImageURL("/kimchi/green-onion/green-onion-more-info.png")
                    .build();
            kimchiRepository.save(greenOnion1);
        }

        public void radishInitData() {
            Kimchi radish1 = Kimchi.builder()
                    .name("깍뚜기 1kg")
                    .price(17000)
                    .type(KimchiType.R)
                    .summaryInfo("모든 재료 국내산으로 만든 엄마표 깍뚜기!")
                    .imageURL("/kimchi/radish/radish.jpeg")
                    .moreInfoImageURL("/kimchi/radish/radish-more-info.jpeg")
                    .build();
            kimchiRepository.save(radish1);
        }
        public void radishSubInitData() {
            Kimchi radish1 = Kimchi.builder()
                    .name("총각김치 1kg")
                    .price(17000)
                    .type(KimchiType.R)
                    .summaryInfo("모든 재료 국내산으로 만든 엄마표 총각김치!")
                    .imageURL("/kimchi/radish/radish-sub.jpeg")
                    .moreInfoImageURL("/kimchi/radish/radish-sub-more-info.jpeg")
                    .build();
            kimchiRepository.save(radish1);
        }

        public void memberInitData() {
            Member member = Member.builder()
                    .loginId("123")
                    .password(passwordEncoder.encode("123"))
                    .name("지원")
                    .role(Role.USER)
                    .phoneNumber("010-3832-4686")
                    .build();
            Cart cart = new Cart();
            member.setInitCart(cart);
            cart.setInitMember(member);
            cartRepository.save(cart);
            memberRepository.save(member);
        }
    }
}
