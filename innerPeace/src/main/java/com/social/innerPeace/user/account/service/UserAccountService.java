package com.social.innerPeace.user.account.service;

import com.social.innerPeace.entity.Member;
import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.MemberDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface UserAccountService {
    String register(MemberDTO dto, Role role);

    default Member dtoToEntity(MemberDTO dto){
        Member entity = Member.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .pw(dto.getPw())
                .birth(convertStringToLocalDate(dto.getBirth()))
                .phone(dto.getPhone())
                .build();
        return entity;
    }

    default MemberDTO entityToDto(Member entity){
        MemberDTO dto = MemberDTO.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                .pw(entity.getPw())
                .birth(entity.getBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .nickName(entity.getNickName())
                .phone(entity.getPhone())
                .build();
        return dto;
    }

    private LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식에 맞게 수정
        return LocalDate.parse(dateString, formatter);
    }

    MemberDTO compareByEmail(String email);

    MemberDTO findmemberProfile(String loginedMember);

    MemberDTO modifyProfileImage(String loginedMember, MemberDTO dto);

    MemberDTO modifyProfile(String loginedMember, MemberDTO dto);

    String findEmail(String loginedMember);

    boolean modifyPassword(String token, String email, String memberPw);

    String delete(String loginedMember, MemberDTO dto);

    MemberDTO modifyMyinfo(String loginedMember, MemberDTO memberDTO);

    MemberDTO findmemberInfo(String loginedMember);
}
