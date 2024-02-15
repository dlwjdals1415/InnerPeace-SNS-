package com.social.innerPeace.user.userPage.service;

import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.FollowRepository;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
public class HealerUserPageServiceImpl implements HealerUserPageService{
    @Value("${profile.dir}")
    String profile_dir;
    @Autowired
    HealerRepository healerRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    PostRepository postRepository;

    @Override
    public HealerDTO findHealer(String healerNickname) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(healerNickname);
        Healer healer = null;
        if(optionalHealer.isPresent()){
            healer = optionalHealer.get();
        }
        HealerDTO dto = entityToDto(healer);
        String image = healer.getHaelerProfileImage();
        String imagePath = profile_dir + image;
        String base64String = null;
        try {
            byte[] fileBytes = readBytesFromFile(imagePath);
            base64String = encodeBytesToBase64(fileBytes);
        } catch (IOException e) {
            // 예외 처리
        }
        dto.setHaeler_profile_image("data:image/png;base64," + base64String);
        dto.setHealer_follow_count(healerRepository.findFollowingByHealerEmail(healer.getHealerEmail()).size());
        dto.setHealer_follower_count(healerRepository.findFollowersByHealerEmail(healer.getHealerEmail()).size());
        dto.setPost_count(healerRepository.findPostByHealerEmail(healer.getHealerEmail()).size());
        return dto;
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
