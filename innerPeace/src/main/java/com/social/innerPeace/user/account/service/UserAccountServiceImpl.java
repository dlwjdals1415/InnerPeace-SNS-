package com.social.innerPeace.user.account.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.entity.ConfirmationToken;
import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.rest.service.ConfirmationTokenService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService{
    @Value("${profile.dir}")
    String profile_dir;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    FileStore fileStore;

    @Autowired
    private HealerRepository healerRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public String register(HealerDTO dto, Role role) {
        if(healerRepository.findById(dto.getHealer_email()).isPresent()){
            return "duplicated";
        }
        Healer healer = dtoToEntity(dto);
        if(dto.getAd_agree()!=null&&!dto.getAd_agree().isEmpty()){
            healer.setAdAgree(true);
        }
        healer.setHealerNickName(getUUID());
        healer.setHealerPw(passwordEncoder.encode(dto.getHealer_pw()));
        healer.setRole(role);
        healer.setHaelerProfileImage("userbaseprofile.jpg");
        healer = healerRepository.save(healer);
        return healer.getHealerEmail();
    }

    @Override
    public HealerDTO compareByEmail(String email) {
        Optional<Healer> optionalHealer = healerRepository.findById(email);
        if(optionalHealer.isPresent()){
            Healer healer = optionalHealer.get();
            return entityToDto(healer);
        }
        return null;
    }

    public String getUUID(){
        return UUID.randomUUID().toString().substring(0,20);
    }

    @Override
    public HealerDTO findHealerProfile(String loginedHealer) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginedHealer);
        if(optionalHealer.isPresent()){
            Healer healer = optionalHealer.get();
            String image = healer.getHaelerProfileImage();
            String imagePath = profile_dir + image;
            String base64String = null;
            try {
                byte[] fileBytes = readBytesFromFile(imagePath);
                base64String = encodeBytesToBase64(fileBytes);
            } catch (IOException e) {
                // 예외 처리
            }
            HealerDTO healerDTO = HealerDTO.builder()
                    .healer_nickname(healer.getHealerNickName())
                    .healer_statusmessage(healer.getHealerStatusmessage())
                    .healer_gender(healer.getHealerGender())
                    .haeler_profile_image("data:image/png;base64," + base64String)
                    .build();
            return healerDTO;
        }
        return null;
    }

    @Override
    public HealerDTO modifyProfileImage(String loginedHealer, HealerDTO dto) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginedHealer);
        if(optionalHealer.isPresent()){
            Healer healer = optionalHealer.get();
            dto = fileStore.profileImage(dto);
            healer.setHaelerProfileImage(dto.getHaeler_profile_image());
            healer = healerRepository.save(healer);

            return entityToDto(healer);
        }
        return null;
    }

    @Override
    public HealerDTO modifyProfile(String loginedHealer, HealerDTO dto) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginedHealer);
        if(optionalHealer.isPresent()) {
            Healer healer = optionalHealer.get();
            if (!dto.getHealer_nickname().equals(loginedHealer)) {
                // 새로운 닉네임이 이미 존재하는지 확인
                Optional<Healer> existingHealer = healerRepository.findByHealerNickName(dto.getHealer_nickname());
                if (existingHealer.isPresent()) {
                    healer.setHealerNickName(healer.getHealerNickName());
                }else{
                    healer.setHealerNickName(dto.getHealer_nickname());
                }
            }
            healer.setHealerStatusmessage(dto.getHealer_statusmessage());
            healer.setHealerGender(dto.getHealer_gender());
            healer = healerRepository.save(healer);

            return entityToDto(healer);
        }
        return null;
    }

    @Override
    public String findEmail(String loginedHealer) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginedHealer);
        if (optionalHealer.isPresent()) {
            Healer healer = optionalHealer.get();
            return healer.getHealerEmail();
        }
        return null;
    }

    @Override
    public boolean modifyPassword(String token, String email, String healerPw) {
        Optional<Healer> optionalHealer = healerRepository.findById(email);
        if (optionalHealer.isPresent()) {
            Healer healer = optionalHealer.get();
            healer.setHealerPw(passwordEncoder.encode(healerPw));
            healerRepository.save(healer);
            ConfirmationToken findConfirmationToken = null;
            try {
                findConfirmationToken = confirmationTokenService.findByIdAndExpirationDateAfterAndExpired(token);
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
            findConfirmationToken.useToken();
            return true;
        }
        return false;
    }

    @Override
    public String delete(String loginedHealer, HealerDTO dto) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginedHealer);
        if (optionalHealer.isPresent()) {
            Healer healer = optionalHealer.get();
            if(passwordEncoder.matches(dto.getHealer_pw(),healer.getHealerPw())){
                return healer.getHealerEmail();
            }else{
                return "비밀번호";
            }
        }
        return null;
    }

    @Override
    public HealerDTO findHealerInfo(String loginedHealer) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginedHealer);
        if(optionalHealer.isPresent()){
            Healer healer = optionalHealer.get();
            HealerDTO healerDTO = HealerDTO.builder()
                    .healer_name(healer.getHealerName())
                    .healer_birth(healer.getHealerBitrh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();
            return healerDTO;
        }
        return null;
    }

    @Override
    public HealerDTO modifyMyinfo(String loginedHealer, HealerDTO healerDTO) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginedHealer);
        if (optionalHealer.isPresent() && healerDTO != null) {
            Healer healer = optionalHealer.get();

            if (healerDTO.getHealer_name() != null) {
                healer.setHealerName(healerDTO.getHealer_name());
            }

            if (healerDTO.getHealer_birth() != null) {
                healer.setHealerBitrh(LocalDate.parse(healerDTO.getHealer_birth()));
            }

            healer = healerRepository.save(healer);

            if (healer != null) {
                return HealerDTO.builder()
                        .healer_nickname(healer.getHealerNickName())
                        .healer_name(healer.getHealerName())
                        .healer_birth(String.valueOf(healer.getHealerBitrh()))
                        .build();
            }
        }
        return null;
    }

    private static byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }

        return fileBytes;
    }

    // 바이트 배열을 Base64로 인코딩하는 메서드
    private static String encodeBytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
