package supportkim.shoppingmall.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class SampleController {

    @Autowired
    private Environment env;



    @GetMapping("/current-port")
    public String sample() {
        String curPortInfo = " 현재 Port : ";
        String property = env.getProperty("local.server.port");
        curPortInfo+=property;
        log.info("port = {} " , env.getProperty("local.server.port"));
        return curPortInfo;
    }
}
