package com.social.innerPeace.user.account.service;

import com.social.innerPeace.dto.SignupDTO;
import com.social.innerPeace.entity.Healer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface UserAccountService {
    Object findByEmail(String email);

    String register(SignupDTO dto);

    default Healer dtoToEntity(SignupDTO dto){
        Healer entity = Healer.builder()
                .healer_email(dto.getEmail())
                .healer_name(dto.getName())
                .healer_pw(dto.getPassword())
                .healer_bitrh(convertStringToLocalDate(dto.getBirth()))
                .healer_phone(dto.getPhone())
                .build();
        return entity;
    }

    default SignupDTO entityToDto(Healer entity){
        SignupDTO dto = SignupDTO.builder()
                .email(entity.getHealer_email())
                .name(entity.getHealer_name())
                .password(entity.getHealer_pw())
                .birth(entity.getHealer_bitrh().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .phone(entity.getHealer_phone())
                .build();
        return dto;
    }

    private LocalDate convertStringToLocalDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 날짜 형식에 맞게 수정
        return LocalDate.parse(dateString, formatter);
    }

}
