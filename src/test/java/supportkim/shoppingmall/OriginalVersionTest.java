//package supportkim.shoppingmall;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import supportkim.shoppingmall.api.dto.OrderResponseDto;
//import supportkim.shoppingmall.domain.Cart;
//import supportkim.shoppingmall.domain.Kimchi;
//import supportkim.shoppingmall.domain.Order;
//import supportkim.shoppingmall.domain.OrderKimchi;
//import supportkim.shoppingmall.domain.member.Member;
//import supportkim.shoppingmall.repository.KimchiRepository;
//import supportkim.shoppingmall.repository.MemberRepository;
//import supportkim.shoppingmall.repository.OrderRepository;
//import supportkim.shoppingmall.service.OrderService;
//
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@SpringBootTest
//public class OriginalVersionTest {
//
//    // 여기서 테스트 코드 작성 -> 기존에 있던걸로 한 번 테스트 해보기
//
//    @Autowired
//    private OrderService orderService;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private KimchiRepository kimchiRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @BeforeEach
//    public void before() {
//        Member member = new Member();
//        member.forTestInitMemberName("태스트");
//        Cart cart = new Cart();
//        cart.initSingUp(member);
//
//        Kimchi kimchi = new Kimchi();
//        kimchi.forTestInitQuantityOrName("테스트김치" , 100);
//
//        OrderKimchi orderKimchi = new OrderKimchi();
//        orderKimchi.forTestInitKimchi(kimchi);
//        cart.forTestInitKimchi(List.of(orderKimchi));
//        memberRepository.saveAndFlush(member);
//        kimchiRepository.saveAndFlush(kimchi);
//    }
//
//
//
//    @Test
//    public void 동시에_요청_100개() throws InterruptedException {
//        int threadCount = 100;
//
//        // 3개의 스레드를 통해 비동기 작업을 실행 ->  100개의 요청이 모두 완료될 때 까지 기다린다.
//        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        Member member = memberRepository.findByName("테스트");
//
//        for (int i = 0; i < threadCount; i++) {
//
//            executorService.submit(() -> {
//                try {
//                    OrderResponseDto.CompleteOrder completeOrder = orderService.cartOrderNoRequest(member);
//                    Order order = orderRepository.findById(completeOrder.getOrderId()).orElseThrow();
//                    System.out.println("주문 금액 : " + order.getOrderPrice());
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        Kimchi kimchi = kimchiRepository.findByName("테스트김치");
//
//        // 기존에 100개의 수량을 가지고 있었고 동시에 100개의 요청이 들어온다면 -> 수량은 0 개가 남을 것
//        System.out.println("현재 남은 개수 : " + kimchi.getQuantity());
//        Assertions.assertEquals(kimchi.getQuantity(),0);
//
//    }
//
//}
