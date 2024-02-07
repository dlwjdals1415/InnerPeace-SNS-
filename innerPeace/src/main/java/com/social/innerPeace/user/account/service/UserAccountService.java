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
                .healer_email(dto.getHealer_email())
                .healer_name(dto.getHealer_name())
                .healer_pw(dto.getHealer_pw())
                .healer_bitrh(convertStringToLocalDate(dto.getHealer_birth()))
                .healer_phone(dto.getHealer_phone())
                .build();
        return entity;
    }

    default HealerDTO entityToDto(Healer entity){
        HealerDTO dto = HealerDTO.builder()
                .healer_email(entity.getHealer_email())
                .healer_name(entity.getHealer_name())
                .healer_pw(entity.getHealer_pw())
                .healer_birth(entity.getHealer_bitrh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .healer_phone(entity.getHealer_phone())
                .build();
        return dto;
    }

    private LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식에 맞게 수정
        return LocalDate.parse(dateString, formatter);
    }

    HealerDTO compareByEmail(String email);
}
