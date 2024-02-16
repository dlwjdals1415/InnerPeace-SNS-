package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.FollowRepository;
import com.social.innerPeace.repository.HealerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Service
public class HealerFollowServiceImpl implements HealerFollowService{
    @Autowired
    private HealerRepository healerRepository;
    @Autowired
    private FollowRepository followRepository;
    @Value("${profile.dir}")
    String profile_dir;

    private final Map<String, String> imageCache = new HashMap<>();

    @Override
    public String follow(String follower, String healerNickname) {
        Optional<Healer> follow = healerRepository.findByHealerNickName(follower);
        if (follow.isEmpty()){
            return null;
        }
        Optional<Healer> healer = healerRepository.findByHealerNickName(healerNickname);
        if (healer.isEmpty()){
            return null;
        }
        Optional<Follow> followOptional = followRepository.findByFollowerHealerEmailAndFollowingHealerEmail(follow.get().getHealerEmail(),healer.get().getHealerEmail());
        if(followOptional.isPresent()){
            followRepository.delete(followOptional.get());
            return "팔로우";
        }else{
            Follow doFollow = Follow.builder()
                    .follower(follow.get())
                    .following(healer.get())
                    .build();
            followRepository.save(doFollow);
            return "팔로우 취소";
        }
    }

    @Transactional
    public List<HealerDTO> findAll(String followingHealer) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(followingHealer);
        Sort sort = Sort.by(Sort.Direction.DESC, "followNo");

        Pageable pageable = PageRequest.of(0, 36, sort);
        Page<Follow> healerList = healerRepository.findByFollower(optionalHealer.get(), pageable);
        return listHealerToDTOList(healerList);
    }

    public List<HealerDTO> listHealerToDTOList(Page<Follow> healerList) {
        if (!healerList.isEmpty()) {
            List<HealerDTO> healerDTOList = toList(healerList);

            try {
                for (HealerDTO healerDTO : healerDTOList) {
                    String profileImagePath = profile_dir + healerDTO.getHaeler_profile_image();
                    String base64String = imageCache.get(profileImagePath);
                    if (base64String == null) {
                        byte[] fileBytes = readBytesFromFile(profileImagePath);
                        base64String = encodeBytesToBase64(fileBytes);
                        imageCache.put(profileImagePath, base64String);
                    }
                    healerDTO.setHaeler_profile_image("data:image/png;base64," + base64String);
                }
            } catch (IOException e) {
                // 예외 처리
            }

            return healerDTOList;
        }
        return null;
    }


    private HealerDTO healerToDTO(Healer healer) {
        return HealerDTO.builder()
                .healer_nickname(healer.getHealerNickName())
                .haeler_profile_image(healer.getHaelerProfileImage())
                .build();
    }

    byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }

        return fileBytes;
    }

    String encodeBytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
