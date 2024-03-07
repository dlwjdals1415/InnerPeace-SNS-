package com.social.innerPeace.user.account.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.entity.ConfirmationToken;
import com.social.innerPeace.entity.Member;
import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.MemberDTO;
import com.social.innerPeace.repository.MemberRepository;
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
    private MemberRepository memberRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Override
    public String register(MemberDTO dto, Role role) {
        if(memberRepository.findById(dto.getEmail()).isPresent()){
            return "duplicated";
        }
        Member member = dtoToEntity(dto);
        if(dto.getAd_agree()!=null&&!dto.getAd_agree().isEmpty()){
            member.setAdAgree(true);
        }
        member.setNickName(getUUID());
        member.setPw(passwordEncoder.encode(dto.getPw()));
        member.setRole(role);
        member.setProfileImage("userbaseprofile.jpg");
        member = memberRepository.save(member);
        return member.getEmail();
    }

    @Override
    public MemberDTO compareByEmail(String email) {
        Optional<Member> optionalHealer = memberRepository.findById(email);
        if(optionalHealer.isPresent()){
            Member member = optionalHealer.get();
            return entityToDto(member);
        }
        return null;
    }

    public String getUUID(){
        return UUID.randomUUID().toString().substring(0,20);
    }

    @Override
    public MemberDTO findmemberProfile(String loginedMember) {
        Optional<Member> optionalmember = memberRepository.findByNickName(loginedMember);
        if(optionalmember.isPresent()){
            Member member = optionalmember.get();
            String image = member.getProfileImage();
            String imagePath = profile_dir + image;
            String base64String = null;
            try {
                byte[] fileBytes = readBytesFromFile(imagePath);
                base64String = encodeBytesToBase64(fileBytes);
            } catch (IOException e) {
                // 예외 처리
            }
            MemberDTO memberDTO = MemberDTO.builder()
                    .nickName(member.getNickName())
                    .statusMessage(member.getStatusMessage())
                    .gender(member.getGender())
                    .profile_image("data:image/png;base64," + base64String)
                    .build();
            return memberDTO;
        }
        return null;
    }

    @Override
    public MemberDTO modifyProfileImage(String loginedMember, MemberDTO dto) {
        Optional<Member> optionalmember = memberRepository.findByNickName(loginedMember);
        if(optionalmember.isPresent()){
            Member member = optionalmember.get();
            dto = fileStore.profileImage(dto);
            member.setProfileImage(dto.getProfile_image());
            member = memberRepository.save(member);

            return entityToDto(member);
        }
        return null;
    }

    @Override
    public MemberDTO modifyProfile(String loginedMember, MemberDTO dto) {
        Optional<Member> optionalmember = memberRepository.findByNickName(loginedMember);
        if(optionalmember.isPresent()) {
            Member member = optionalmember.get();
            if (!dto.getNickName().equals(loginedMember)) {
                // 새로운 닉네임이 이미 존재하는지 확인
                Optional<Member> existingmember = memberRepository.findByNickName(dto.getNickName());
                if (existingmember.isPresent()) {
                    member.setNickName(member.getNickName());
                }else{
                    member.setNickName(dto.getNickName());
                }
            }
            member.setStatusMessage(dto.getStatusMessage());
            member.setGender(dto.getGender());
            member = memberRepository.save(member);

            return entityToDto(member);
        }
        return null;
    }

    @Override
    public String findEmail(String loginedMember) {
        Optional<Member> optionalmember = memberRepository.findByNickName(loginedMember);
        if (optionalmember.isPresent()) {
            Member member = optionalmember.get();
            return member.getEmail();
        }
        return null;
    }

    @Override
    public boolean modifyPassword(String token, String email, String memberPw) {
        Optional<Member> optionalmember = memberRepository.findById(email);
        if (optionalmember.isPresent()) {
            Member member = optionalmember.get();
            member.setPw(passwordEncoder.encode(memberPw));
            memberRepository.save(member);
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
    public String delete(String loginedMember, MemberDTO dto) {
        Optional<Member> optionalmember = memberRepository.findByNickName(loginedMember);
        if (optionalmember.isPresent()) {
            Member member = optionalmember.get();
            if(passwordEncoder.matches(dto.getPw(), member.getPw())){
                return member.getEmail();
            }else{
                return "비밀번호";
            }
        }
        return null;
    }

    @Override
    public MemberDTO findmemberInfo(String loginedMember) {
        Optional<Member> optionalmember = memberRepository.findByNickName(loginedMember);
        if(optionalmember.isPresent()){
            Member member = optionalmember.get();
            MemberDTO memberDTO = MemberDTO.builder()
                    .name(member.getName())
                    .birth(member.getBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .build();
            return memberDTO;
        }
        return null;
    }

    @Override
    public MemberDTO modifyMyinfo(String loginedMember, MemberDTO memberDTO) {
        Optional<Member> optionalmember = memberRepository.findByNickName(loginedMember);
        if (optionalmember.isPresent() && memberDTO != null) {
            Member member = optionalmember.get();

            if (memberDTO.getName() != null) {
                member.setName(memberDTO.getName());
            }

            if (memberDTO.getBirth() != null) {
                member.setBirth(LocalDate.parse(memberDTO.getBirth()));
            }

            member = memberRepository.save(member);

            if (member != null) {
                return MemberDTO.builder()
                        .nickName(member.getNickName())
                        .name(member.getName())
                        .birth(String.valueOf(member.getBirth()))
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
