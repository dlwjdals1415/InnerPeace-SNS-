package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.FollowDTO;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
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
import java.util.stream.Collectors;

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

    @Override
    public List<FollowDTO> findFollowing(String healerNickname) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(healerNickname);
        if (optionalHealer.isPresent()) {
            Healer healer = optionalHealer.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "followNo");

            Pageable pageable = PageRequest.of(0, 36, sort);
            List<Follow> followList = healerRepository.findByFollowingHealerEmail(healer.getHealerEmail(), pageable);

            // Follow 엔티티를 FollowDTO로 변환
            List<FollowDTO> followDTOList = followList.stream()
                    .map(follow -> FollowDTO.builder()
                            .follow_no(follow.getFollowNo())
                            .follow(follow.getFollower().getHealerEmail())
                            .healer_nickname(follow.getFollower().getHealerNickName())
                            .healer_profile_image(findProfileImageBase64(follow.getFollower().getHaelerProfileImage()))
                            .build())
                    .collect(Collectors.toList());
            for(FollowDTO followDTO : followDTOList){
                Optional<Follow> follow = followList.stream().filter(f -> f.getFollowing().getHealerNickName().equals(healer.getHealerNickName())).findFirst();
                if (follow.isPresent()) {
                    followDTO.setFollowstatus("팔로우 취소");
                } else {
                    followDTO.setFollowstatus("팔로우");
                }
            }
            return followDTOList;
        }

        return null;
    }

    @Override
    public List<FollowDTO> findFollower(String healerNickname) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(healerNickname);
        if (optionalHealer.isPresent()) {
            Healer healer = optionalHealer.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "followNo");

            Pageable pageable = PageRequest.of(0, 36, sort);
            List<Follow> followList = healerRepository.findByFollowerHealerEmail(healer.getHealerEmail(), pageable);

            // Follow 엔티티를 FollowDTO로 변환
            List<FollowDTO> followDTOList = followList.stream()
                    .map(follow -> FollowDTO.builder()
                            .follow_no(follow.getFollowNo())
                            .follow(follow.getFollowing().getHealerEmail())
                            .healer_nickname(follow.getFollowing().getHealerNickName())
                            .healer_profile_image(findProfileImageBase64(follow.getFollowing().getHaelerProfileImage()))
                            .build())
                    .collect(Collectors.toList());
            for(FollowDTO followDTO : followDTOList){
                Optional<Follow> follow = followList.stream().filter(f -> f.getFollowing().getHealerNickName().equals(healer.getHealerNickName())).findFirst();
                if (follow.isPresent()) {
                    followDTO.setFollowstatus("팔로우 취소");
                } else {
                    followDTO.setFollowstatus("팔로우");
                }
            }
            return followDTOList;
        }

        return null;
    }

    private String findProfileImageBase64(String profileImagePath) {
        String imagePath = profile_dir + profileImagePath;
        String base64String = imageCache.get(imagePath);
        if (base64String == null) {
            try {
                byte[] fileBytes = readBytesFromFile(imagePath);
                base64String = encodeBytesToBase64(fileBytes);
                imageCache.put(imagePath, base64String);
            } catch (IOException e) {
                // 예외 처리
            }
        }
        return "data:image/png;base64," + base64String;
    }


    public HealerDTO findProfileImagename(String healerEmail) {
        Healer healer = healerRepository.findByHealerNickName(healerEmail).orElse(null);
        assert healer != null;
        HealerDTO dto = HealerDTO.builder()
                .haeler_profile_image(healer.getHaelerProfileImage())
                .build();
        return dto;
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
