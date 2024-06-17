package supportkim.shoppingmall.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.AlarmResponseDto;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.domain.alarm.Alarm;
import supportkim.shoppingmall.domain.alarm.AlarmArgs;
import supportkim.shoppingmall.domain.alarm.AlarmType;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.jwt.JwtService;
import supportkim.shoppingmall.repository.AlarmRepository;
import supportkim.shoppingmall.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

import static supportkim.shoppingmall.api.dto.AlarmResponseDto.*;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    @Transactional
    public void send(AlarmType type , Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
        Alarm alarm = Alarm.of(type, member);
        alarmRepository.save(alarm);
        /**
         * SSE 로 변경하기
         */
    }


    public MultiAlarm getAlarmList(HttpServletRequest request) {

        Member member = findMemberFromAccessToken(request);
        List<SingleAlarm> result = alarmRepository.findAllByMemberId(member.getId()).stream()
                .map(alarm -> new SingleAlarm(alarm.getAlarmType()))
                .toList();
        return new MultiAlarm(result);
    }

    private Member findMemberFromAccessToken(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_ACCESS_TOKEN));
        String email = jwtService.extractMemberEmail(accessToken);
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_MEMBER));
    }
}
