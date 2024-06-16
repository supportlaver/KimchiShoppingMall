package supportkim.shoppingmall.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import supportkim.shoppingmall.domain.alarm.AlarmArgs;
import supportkim.shoppingmall.domain.alarm.AlarmType;

import java.util.List;

public class AlarmResponseDto {

    @Builder @Getter
    public static class SingleAlarm {
        private AlarmType alarmType;

        public SingleAlarm(AlarmType alarmType) {
            this.alarmType = alarmType;
        }
    }

    @Getter @Builder
    public static class MultiAlarm {
        private List<SingleAlarm> singleAlarmList;

        public MultiAlarm(List<SingleAlarm> singleAlarmList) {
            this.singleAlarmList = singleAlarmList;
        }
    }


}
