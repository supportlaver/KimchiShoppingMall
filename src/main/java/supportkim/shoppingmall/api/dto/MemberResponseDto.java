package supportkim.shoppingmall.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import supportkim.shoppingmall.domain.Coupon;
import supportkim.shoppingmall.domain.alarm.AlarmType;
import supportkim.shoppingmall.domain.member.Member;
import supportkim.shoppingmall.jwt.TokenMapping;

public class MemberResponseDto {

    @Builder @Getter
    public static class SingleMember {
        private Long memberId;
        private String name;
        private String email;

        @JsonCreator
        public SingleMember(@JsonProperty("id") Long id , @JsonProperty("name") String name,
                            @JsonProperty("email") String email) {
            this.memberId = id;
            this.name = name;
            this.email = email;
        }

        public static SingleMember from(Long memberId, String name, String email) {
            return SingleMember.builder()
                    .memberId(memberId)
                    .name(name)
                    .email(email)
                    .build();
        }

        public static Member fromMemberEntity(Long memberId, String name, String email) {
            return Member.builder()
                    .id(memberId)
                    .name(name)
                    .email(email)
                    .build();
        }
    }

    @Builder @Getter
    public static class SignUp {
        private Long memberId;
        private String couponName;
        private int couponDiscountValue;

        public static SignUp from(Member member , Coupon coupon) {
            return SignUp.builder()
                    .memberId(member.getId())
                    .couponName(coupon.getDiscountInfo())
                    .couponDiscountValue(coupon.getDiscountValue())
                    .build();
        }
    }

    @Builder @Getter
    public static class SignUpWithAlarm {
        private Long memberId;
        private String couponName;
        private int couponDiscountValue;
        private AlarmType alarmType;

        public static SignUpWithAlarm from(Member member , Coupon coupon , AlarmType alarmType) {
            return SignUpWithAlarm.builder()
                    .memberId(member.getId())
                    .couponName(coupon.getDiscountInfo())
                    .couponDiscountValue(coupon.getDiscountValue())
                    .alarmType(alarmType)
                    .build();
        }
    }

    @Builder @Getter
    public static class Login {
        private String name;
        private Long id;
        private TokenMapping tokenMapping;

        public static Login from(Member member , TokenMapping token) {
            return Login.builder()
                    .id(member.getId())
                    .tokenMapping(token)
                    .name(member.getName())
                    .build();
        }
    }
}
