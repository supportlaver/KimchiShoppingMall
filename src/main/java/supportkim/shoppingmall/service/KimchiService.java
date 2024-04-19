package supportkim.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import supportkim.shoppingmall.api.dto.KimchiResponseDto;
import supportkim.shoppingmall.domain.Kimchi;
import supportkim.shoppingmall.exception.BaseException;
import supportkim.shoppingmall.exception.ErrorCode;
import supportkim.shoppingmall.repository.KimchiRepository;

import java.util.List;
import java.util.stream.Collectors;

import static supportkim.shoppingmall.api.dto.KimchiResponseDto.*;

@Service
@RequiredArgsConstructor
public class KimchiService {

    private final KimchiRepository kimchiRepository;

    // 단건 조회
    public SingleKimchi findOne(Long kimchiId) {
        Kimchi kimchi = kimchiRepository.findById(kimchiId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_KIMCHI));
        return SingleKimchi.from(kimchi);
    }

    // 모두 조회
    public KimchiList findAll() {
        List<Kimchi> findKimchiList = kimchiRepository.findAll();
        List<SingleKimchi> result = findKimchiList.stream().map(SingleKimchi::from)
                .toList();
        return KimchiList.from(result);
    }
}
