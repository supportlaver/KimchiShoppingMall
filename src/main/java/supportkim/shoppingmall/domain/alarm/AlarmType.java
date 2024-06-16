package supportkim.shoppingmall.domain.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    COMPLETE_ORDER("주문이 완료됐습니다!"),
    CONGRATULATION_SIGN_UP("회원가입을 축하합니다. (쿠폰 지급)");
    private final String alarmText;
}
