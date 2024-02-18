package com.social.innerPeace.user.account.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.HealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    @Override
    public String register(HealerDTO dto, Role role) {
        if(healerRepository.findById(dto.getHealer_email()).isPresent()){
            return "duplicated";
        }
        Healer healer = dtoToEntity(dto);
        if(!dto.getAd_agree().isEmpty()){
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
            healer = healerRepository.save(healer);
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
