package supportkim.shoppingmall.global.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import supportkim.shoppingmall.service.KimchiService;

@Configuration
@Slf4j
public class GlobalConfig {
    @Bean
    public CountedAspect countedAspect(MeterRegistry meterRegistry) {
        return new CountedAspect(meterRegistry);
    }

    @Bean
    public MeterBinder getStock(KimchiService kimchiService) {
        return registry -> Gauge.builder("stock" , kimchiService , service -> {
            log.info("get Stock Call for Gauge");
            return service.getStock();
        }).register(registry);
    }
}
