package supportkim.shoppingmall.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import supportkim.shoppingmall.domain.alarm.AlarmEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {
    private final KafkaTemplate<Long , AlarmEvent> alarmEventKafkaTemplate;
    public void send(AlarmEvent event) {
        alarmEventKafkaTemplate.send("alarm" , event.getReceiverMemberId() , event);
        log.info("send finish!");
    }
}
