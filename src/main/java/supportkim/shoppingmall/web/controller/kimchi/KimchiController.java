package supportkim.shoppingmall.web.controller.kimchi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KimchiController {

    @GetMapping("/baechu")
    public String baechu() {
        return "kimchi/shop";
    }



}
