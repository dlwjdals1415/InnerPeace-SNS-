package com.social.innerPeace.user.account.service;

import com.social.innerPeace.dto.SignupDTO;
import com.social.innerPeace.entity.Healer;

public interface UserAccountService {
    Object findByEmail(String email);

    String register(SignupDTO dto);

    default Healer dtoToEntity(SignupDTO dto){
        Healer entity = Healer.builder()
                .healer_email(dto.getEmail())
                .healer_name(dto.getName())
                .healer_pw(dto.getPassword())
                .healer_bitrh(dto.getBirth())
                .healer_phone(dto.getPhone())
                .build();
        return entity;
    }

    default SignupDTO entityToDto(Healer entity){
        SignupDTO dto = SignupDTO.builder()
                .email(entity.getHealer_email())
                .name(entity.getHealer_name())
                .password(entity.getHealer_pw())
                .birth(entity.getHealer_bitrh())
                .phone(entity.getHealer_phone())
                .build();
        return dto;
    }

    SignupDTO login(SignupDTO dto);
}
