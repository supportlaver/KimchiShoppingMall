package supportkim.shoppingmall.web.controller.kimchi;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.repository.KimchiRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class KimchiController {
    
    private final KimchiRepository kimchiRepository;
    @GetMapping("/baechus")
    public String baechu(Model model) {
        List<Kimchi> baeChus = kimchiRepository.findAllBaeChu();
        model.addAttribute("baechus",baeChus);
        return "kimchi/baechu/baechus";
    }

    @GetMapping("/baechus-{kimchi}-detail")
    public String baeChuDetail(@PathVariable("kimchi") Long id, Model model) {
        Kimchi findKimchi = kimchiRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("kimchi",findKimchi);
        return "kimchi/baechu/baechuDetail";
    }
}
