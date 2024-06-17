package supportkim.shoppingmall.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import supportkim.shoppingmall.domain.alarm.AlarmEvent;
import supportkim.shoppingmall.service.AlarmService;

import java.time.temporal.ValueRange;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmConsumer {
    private final AlarmService alarmService;
    @KafkaListener(topics = "alarm")
    public void consumeAlarm(AlarmEvent event , Acknowledgment ack) {
        alarmService.send(event.getType() ,event.getReceiverMemberId());
        ack.acknowledge();
    }
}
