package supportkim.shoppingmall.web.controller.kimchi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.repository.KimchiRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class KimchiController {
    
    private final KimchiRepository kimchiRepository;

    // 여기 부분을 AOP 를 넣어서 최적화 시킬 수 있나?
    @GetMapping("/baechus")
    public String baechu(Model model) {
        List<Kimchi> baeChus = kimchiRepository.findAllBaeChu();
        model.addAttribute("baechus",baeChus);
        return "kimchi/baechu/baechus";
    }

    @GetMapping("/yeolmus")
    public String yeolmu(Model model) {
        List<Kimchi> yeolmus = kimchiRepository.findAllYeolmu();
        model.addAttribute("yeolmus",yeolmus);
        return "kimchi/yeolmu/yeolmus";
    }
    @GetMapping("/radishs")
    public String radish(Model model) {
        List<Kimchi> radishs = kimchiRepository.findAllRadish();
        model.addAttribute("radishs",radishs);
        return "kimchi/radish/radishs";
    }

    @GetMapping("/green-onions")
    public String greenOnions(Model model) {
        List<Kimchi> greenOnions = kimchiRepository.findAllGreenOnion();
        model.addAttribute("greenOnions",greenOnions);
        return "kimchi/green-onion/green-onions";
    }


    @GetMapping("/baechus-{kimchi}-detail")
    public String baeChuDetail(@PathVariable("kimchi") Long id, Model model) {
        Kimchi findKimchi = kimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        log.info("findKimchi = {}",findKimchi.getName());
        model.addAttribute("kimchi",findKimchi);
        return "kimchi/detail";
    }

    @GetMapping("/yeolmus-{kimchi}-detail")
    public String yeolmuDetail(@PathVariable("kimchi") Long id, Model model) {
        Kimchi findKimchi = kimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("kimchi",findKimchi);
        return "kimchi/detail";
    }

    @GetMapping("/radishs-{kimchi}-detail")
    public String radishDetail(@PathVariable("kimchi") Long id, Model model) {
        Kimchi findKimchi = kimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("kimchi",findKimchi);
        return "kimchi/detail";
    }

    @GetMapping("/green-onion-{kimchi}-detail")
    public String greenOnionDetail(@PathVariable("kimchi") Long id, Model model) {
        Kimchi findKimchi = kimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("kimchi",findKimchi);
        return "kimchi/detail";
    }

    @GetMapping("/baechus/{kimchi}/detail")
    public String baeChuDetail_sub(@PathVariable("kimchi") Long id, Model model) {
        Kimchi findKimchi = kimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        log.info("findKimchi = {}",findKimchi.getName());
        model.addAttribute("kimchi",findKimchi);
        return "kimchi/detail";
    }
}
