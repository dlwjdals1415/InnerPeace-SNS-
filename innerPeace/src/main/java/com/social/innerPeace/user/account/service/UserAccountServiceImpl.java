package com.social.innerPeace.user.account.service;

import com.social.innerPeace.ip_enum.Role;
import com.social.innerPeace.dto.SignupDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.HealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private HealerRepository healerRepository;

    @Override
    public String register(SignupDTO dto, Role role) {
        if(healerRepository.findById(dto.getEmail()).isPresent()){
            return "duplicated";
        }
        Healer healer = dtoToEntity(dto);
        healer.setHealer_nickname(dto.getEmail());
        healer.setHealer_pw(passwordEncoder.encode(dto.getPassword()));
        healer.setRole(role);
        healer = healerRepository.save(healer);
        return healer.getHealerEmail();
    }

    @Override
    public SignupDTO compareByEmail(String email) {
        Optional<Healer> optionalHealer = healerRepository.findById(email);
        if(optionalHealer.isPresent()){
            Healer healer = optionalHealer.get();
            healer = healerRepository.save(healer);
            return entityToDto(healer);
        }
        return null;
    }
}
