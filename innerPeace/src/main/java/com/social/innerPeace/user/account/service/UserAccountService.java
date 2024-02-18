package com.social.innerPeace.user.account.service;

import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Healer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public interface UserAccountService {
    String register(HealerDTO dto, Role role);

    default Healer dtoToEntity(HealerDTO dto){
        Healer entity = Healer.builder()
                .healerEmail(dto.getHealer_email())
                .healerName(dto.getHealer_name())
                .healerPw(dto.getHealer_pw())
                .healerBitrh(convertStringToLocalDate(dto.getHealer_birth()))
                .healerPhone(dto.getHealer_phone())
                .build();
        return entity;
    }

    default HealerDTO entityToDto(Healer entity){
        HealerDTO dto = HealerDTO.builder()
                .healer_email(entity.getHealerEmail())
                .healer_name(entity.getHealerName())
                .healer_pw(entity.getHealerPw())
                .healer_birth(entity.getHealerBitrh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .healer_nickname(entity.getHealerNickName())
                .healer_phone(entity.getHealerPhone())
                .build();
        return dto;
    }

    private LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식에 맞게 수정
        return LocalDate.parse(dateString, formatter);
    }

    HealerDTO compareByEmail(String email);

    HealerDTO findHealerProfile(String loginedHealer);

    HealerDTO modifyProfileImage(String loginedHealer, HealerDTO dto);

    HealerDTO modifyProfile(String loginedHealer, HealerDTO dto);

    String findEmail(String loginedHealer);

    boolean modifyPassword(String token, String email, String healerPw);

    String delete(String loginedHealer, HealerDTO dto);
}
