package com.social.innerPeace.rest.service;

import com.mysema.commons.lang.Assert;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.ConfirmationToken;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.ConfirmationTokenRepository;
import com.social.innerPeace.repository.HealerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final HealerRepository healerRepository;
    private final EmailService emailService;
    /**
     * 이메일 인증 토큰 생성
     * @return
     */
    public String createEmailConfirmationToken(String email){

        Assert.hasText(email,"receiverEmail은 필수 입니다.");

        ConfirmationToken emailConfirmationToken = ConfirmationToken.createEmailConfirmationToken(email);
        confirmationTokenRepository.save(emailConfirmationToken);

        Healer healer = healerRepository.findById(email).get();
        healer.setEmailVerified(false);
        healerRepository.save(healer);

        String subject = "[InnerPeace] 인증번호 안내";
        String text = "안녕하세요\n" +
                "\n" +
                "[Inner Peace] 이용을 위한 인증URL를 안내드립니다.\n" +
                "\n" +
                "인증URL: [" + "http://localhost:8080/confirm-email?token="+emailConfirmationToken.getId() +"]\n" +
                "\n" +
                "위 인증번호를 홈페이지 또는 앱에서 요구하는 인증번호 입력란에 정확히 입력해 주세요. 이 인증번호는 발급 후 [10분] 동안만 유효합니다.\n" +
                "\n" +
                "인증 과정에서 문제가 발생하거나 도움이 필요하시면, 언제든지 [고객지원 센터 연락처 또는 이메일 주소]로 문의해 주세요.\n" +
                "\n" +
                "감사합니다.\n" +
                "\n" +
                "[Inner Peace] 팀 드림";
        emailService.sendSimpleMessage(email, subject, text);

        return emailConfirmationToken.getId();
    }

    /**
     * 유효한 토큰 가져오기
     * @param confirmationTokenId
     * @return
     */
    public ConfirmationToken findByIdAndExpirationDateAfterAndExpired(String confirmationTokenId) throws BadRequestException {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByIdAndExpirationDateAfterAndExpired(confirmationTokenId, LocalDateTime.now(),false);
        return confirmationToken.orElseThrow(()-> new BadRequestException("Token not found"));
    };

    public void confirmEmail(String token) {
        ConfirmationToken findConfirmationToken = null;
        try {
            findConfirmationToken = findByIdAndExpirationDateAfterAndExpired(token);
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        Optional<Healer> optionalHealer = healerRepository.findById(findConfirmationToken.getEmail());
        findConfirmationToken.useToken();	// 토큰 만료 로직을 구현해주면 된다. ex) expired 값을 true로 변경
        Healer healer = optionalHealer.get();
        healer.setEmailVerified(true);
        healerRepository.save(healer);// 유저의 이메일 인증 값 변경 로직을 구현해주면 된다. ex) emailVerified 값을 true로 변경
    }

    @Scheduled(cron = "0 0 * * * *") // 매시간마다 실행
    public void deleteExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<ConfirmationToken> expiredTokens = confirmationTokenRepository.findAllByExpirationDateBeforeAndExpiredIsFalse(now);
        confirmationTokenRepository.deleteAll(expiredTokens);
    }

}