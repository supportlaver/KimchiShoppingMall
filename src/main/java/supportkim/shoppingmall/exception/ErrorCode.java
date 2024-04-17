package supportkim.shoppingmall.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * 성공 코드
     */
    SUCCESS(HttpStatus.OK , "success" , "요청에 성공했습니다." ),

    /**
     * 필요한 코드 추가
     */


    /**
     * Authentication (인증 관련 코드)
     */
    NOT_SUPPORT_METHOD(HttpStatus.METHOD_NOT_ALLOWED , "Auth001" , "해당 로직에 필요한 요청 방식이 아닙니다."),
    EMPTY_LOGIN_INFO(HttpStatus.NOT_FOUND , "Auth002" , "아이디 또는 비밀번호가 비어있습니다."),

    /**
     * 토큰 에러
     */
    TOKEN_NOT_VALID(HttpStatus.FORBIDDEN , "T001" , "토큰이 유효하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED , "T002" , "리프레쉬 토큰이 만료됐습니다."),

    /**
     * Member 관련 코드
     */
    NOT_EXIST_MEMBER(HttpStatus.NOT_FOUND , "M001" , "해당 유저가 존재하지 않습니다.");




    private final HttpStatus status;
    private final String code;
    private final String message;

    public static ErrorCode findByMessage(String message) {
        for (ErrorCode response : values()) {
            if (message.equals(response.message)) {
                return response;
            }
        }
        return null;
    }
}
