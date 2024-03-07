package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.FollowDTO;
import com.social.innerPeace.dto.MemberDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Member;
import com.social.innerPeace.repository.FollowRepository;
import com.social.innerPeace.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private FollowRepository followRepository;
    @Value("${profile.dir}")
    String profile_dir;

    private final Map<String, String> imageCache = new HashMap<>();

    @Override
    public String follow(String follower, String healerNickname) {
        Optional<Member> follow = memberRepository.findByNickName(follower);
        if (follow.isEmpty()){
            return null;
        }
        Optional<Member> healer = memberRepository.findByNickName(healerNickname);
        if (healer.isEmpty()){
            return null;
        }
        Optional<Follow> followOptional = followRepository.findByFollowerEmailAndFollowingEmail(follow.get().getEmail(),healer.get().getEmail());
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
    public List<FollowDTO> findFollowing(String healerNickname, String loginedMember) {
        Optional<Member> optionalHealer = memberRepository.findByNickName(healerNickname);
        Optional<Member> optionalloginedMember = memberRepository.findByNickName(loginedMember);
        if (optionalHealer.isPresent()) {
            Member member = optionalHealer.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "followNo");

            Pageable pageable = PageRequest.of(0, 36, sort);
            List<Follow> followList = memberRepository.findByFollowingEmail(member.getEmail(), pageable);

            // Follow 엔티티를 FollowDTO로 변환
            List<FollowDTO> followDTOList = followList.stream()
                    .map(follow -> FollowDTO.builder()
                            .follow_no(follow.getFollowNo())
                            .follow(follow.getFollower().getEmail())
                            .nickName(follow.getFollower().getNickName())
                            .profile_image(findProfileImageBase64(follow.getFollower().getProfileImage()))
                            .build())
                    .collect(Collectors.toList());
            if(optionalloginedMember.isPresent()){
                List<Follow> followerList = memberRepository.findFollowersByEmail(optionalloginedMember.get().getEmail());
                for(Follow follower:followList){
                    for(FollowDTO followDTO : followDTOList){
                        Optional<Follow> follow = followerList.stream().filter(f -> f.getFollowing().getNickName().equals(follower.getFollower().getNickName())).findFirst();
                        if (follow.isPresent()) {
                            followDTO.setFollowStatus("팔로우 취소");
                        } else {
                            followDTO.setFollowStatus("팔로우");
                        }
                    }
                }

            }
            return followDTOList;
        }

        return null;
    }

    @Override
    public List<FollowDTO> findFollower(String healerNickname, String loginedMember) {
        Optional<Member> optionalHealer = memberRepository.findByNickName(healerNickname);
        Optional<Member> optionalloginedMember = memberRepository.findByNickName(loginedMember);
        if (optionalHealer.isPresent()) {
            Member member = optionalHealer.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "followNo");

            Pageable pageable = PageRequest.of(0, 36, sort);
            List<Follow> followList = memberRepository.findByFollowerEmail(member.getEmail(), pageable);

            // Follow 엔티티를 FollowDTO로 변환
            List<FollowDTO> followDTOList = followList.stream()
                    .map(follow -> FollowDTO.builder()
                            .follow_no(follow.getFollowNo())
                            .follow(follow.getFollowing().getEmail())
                            .nickName(follow.getFollowing().getNickName())
                            .profile_image(findProfileImageBase64(follow.getFollowing().getProfileImage()))
                            .build())
                    .collect(Collectors.toList());
            if(optionalloginedMember.isPresent()){
                List<Follow> followerList = memberRepository.findFollowersByEmail(optionalloginedMember.get().getEmail());
                for(Follow follower:followList){
                    for(FollowDTO followDTO : followDTOList){
                        Optional<Follow> follow = followerList.stream().filter(f -> f.getFollowing().getNickName().equals(follower.getFollowing().getNickName())).findFirst();
                        if (follow.isPresent()) {
                            followDTO.setFollowStatus("팔로우 취소");
                        } else {
                            followDTO.setFollowStatus("팔로우");
                        }
                    }
                }

            }
            return followDTOList;
        }

        return null;
    }

    @Override
    public List<FollowDTO> findFollowingScroll(String healerNickname, String loginedMember, long followNo) {
        Optional<Member> optionalHealer = memberRepository.findByNickName(healerNickname);
        Optional<Member> optionalloginedMember = memberRepository.findByNickName(loginedMember);
        if (optionalHealer.isPresent()) {
            Member member = optionalHealer.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "followNo");

            Pageable pageable = PageRequest.of(0, 36, sort);
            List<Follow> followList = memberRepository.findByFollowingEmailAndFollowNoLessThanEqual(member.getEmail(), followNo, pageable);

            // Follow 엔티티를 FollowDTO로 변환
            List<FollowDTO> followDTOList = followList.stream()
                    .map(follow -> FollowDTO.builder()
                            .follow_no(follow.getFollowNo())
                            .follow(follow.getFollower().getEmail())
                            .nickName(follow.getFollower().getNickName())
                            .profile_image(findProfileImageBase64(follow.getFollower().getProfileImage()))
                            .build())
                    .collect(Collectors.toList());
            if(optionalloginedMember.isPresent()){
                List<Follow> followerList = memberRepository.findFollowersByEmail(optionalloginedMember.get().getEmail());
                for(Follow follower:followList){
                    for(FollowDTO followDTO : followDTOList){
                        Optional<Follow> follow = followerList.stream().filter(f -> f.getFollowing().getNickName().equals(follower.getFollower().getNickName())).findFirst();
                        if (follow.isPresent()) {
                            followDTO.setFollowStatus("팔로우 취소");
                        } else {
                            followDTO.setFollowStatus("팔로우");
                        }
                    }
                }

            }
            return followDTOList;
        }

        return null;
    }

    @Override
    public List<FollowDTO> findFollowerScroll(String healerNickname, String loginedMember, long followNo) {
        Optional<Member> optionalHealer = memberRepository.findByNickName(healerNickname);
        Optional<Member> optionalloginedMember = memberRepository.findByNickName(loginedMember);
        if (optionalHealer.isPresent()) {
            Member member = optionalHealer.get();
            Sort sort = Sort.by(Sort.Direction.DESC, "followNo");

            Pageable pageable = PageRequest.of(0, 36, sort);
            List<Follow> followList = memberRepository.findByFollowerEmailAndFollowNoLessThanEqual(member.getEmail(),followNo ,pageable);

            // Follow 엔티티를 FollowDTO로 변환
            List<FollowDTO> followDTOList = followList.stream()
                    .map(follow -> FollowDTO.builder()
                            .follow_no(follow.getFollowNo())
                            .follow(follow.getFollowing().getEmail())
                            .nickName(follow.getFollowing().getNickName())
                            .profile_image(findProfileImageBase64(follow.getFollowing().getProfileImage()))
                            .build())
                    .collect(Collectors.toList());
            if(optionalloginedMember.isPresent()){
                List<Follow> followerList = memberRepository.findFollowersByEmail(optionalloginedMember.get().getEmail());
                for(Follow follower:followList){
                    for(FollowDTO followDTO : followDTOList){
                        Optional<Follow> follow = followerList.stream().filter(f -> f.getFollowing().getNickName().equals(follower.getFollowing().getNickName())).findFirst();
                        if (follow.isPresent()) {
                            followDTO.setFollowStatus("팔로우 취소");
                        } else {
                            followDTO.setFollowStatus("팔로우");
                        }
                    }
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


    public MemberDTO findProfileImagename(String healerEmail) {
        Member member = memberRepository.findByNickName(healerEmail).orElse(null);
        assert member != null;
        MemberDTO dto = MemberDTO.builder()
                .profile_image(member.getProfileImage())
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