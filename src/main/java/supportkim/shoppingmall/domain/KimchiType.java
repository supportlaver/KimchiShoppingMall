package supportkim.shoppingmall.domain;

import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;

public enum KimchiType {
    // B : 배추 김치
    // Y : 열무 김치
    // GO : 파김치
    // R : 깍두기
    B("배추 김치") , Y("열무 김치") , GO("파김치") , R("깍두기");

    private final String name;

    KimchiType(String name) {
        this.name = name;
    }

    public static KimchiType getInstance(String name) {
        for (KimchiType value : KimchiType.values()) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        throw new BaseException(ErrorCode.NOT_EXIST_KIMCHI_TYPE);
    }
}
