package com.social.innerPeace.user.userPage.service;

import com.social.innerPeace.dto.MemberDTO;
import com.social.innerPeace.entity.Member;

public interface UserPageService {
    MemberDTO findHealer(String healerNickname, String loginedHealer);


    default MemberDTO entityToDto(Member member){
        MemberDTO dto = MemberDTO.builder()
                .nickName(member.getNickName())
                .statusMessage(member.getStatusMessage())
                .build();
        return dto;
    }

}
