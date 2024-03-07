package com.social.innerPeace.user.userPage.service;

import com.social.innerPeace.dto.MemberDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Member;
import com.social.innerPeace.repository.FollowRepository;
import com.social.innerPeace.repository.MemberRepository;
import com.social.innerPeace.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class UserPageServiceImpl implements UserPageService {
    @Value("${profile.dir}")
    String profile_dir;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    PostRepository postRepository;

    @Override
    public MemberDTO findHealer(String healerNickname, String loginedHealer) {
        Optional<Member> optionalHealer = memberRepository.findByNickName(healerNickname);
        Optional<Member> optionalloginedHealer = memberRepository.findByNickName(loginedHealer);
        if(!optionalHealer.isPresent()){
           return null;
        }
        Member member = optionalHealer.get();
        MemberDTO dto = entityToDto(member);
        if (optionalloginedHealer.isPresent()) {
            List<Follow> followerList = memberRepository.findFollowersByEmail(member.getEmail());
            Optional<Follow> follow = followerList.stream().filter(f -> f.getFollowing().getNickName().equals(optionalloginedHealer.get().getNickName())).findFirst();
            if (follow.isPresent()) {
                dto.setFollow_status("팔로우 취소");
            } else {
                dto.setFollow_status("팔로우");
            }
        }
        String image = member.getProfileImage();
        String imagePath = profile_dir + image;
        String base64String = null;
        try {
            byte[] fileBytes = readBytesFromFile(imagePath);
            base64String = encodeBytesToBase64(fileBytes);
        } catch (IOException e) {
            // 예외 처리
        }
        dto.setProfile_image("data:image/png;base64," + base64String);
        dto.setFollow_count(memberRepository.findFollowingByEmail(member.getEmail()).size());
        dto.setFollower_count(memberRepository.findFollowersByEmail(member.getEmail()).size());
        dto.setPost_count(memberRepository.findPostByEmail(member.getEmail()).size());
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
