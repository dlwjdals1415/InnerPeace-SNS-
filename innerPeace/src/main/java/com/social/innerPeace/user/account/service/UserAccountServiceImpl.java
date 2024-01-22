package com.social.innerPeace.user.account.service;

import com.social.innerPeace.dto.SignupDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.HealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService{
    private final HealerRepository healerRepository;
    @Override
    public Object findByEmail(String email) {
        Optional<Healer> optionalHealer = healerRepository.findById(email);
        if(optionalHealer.isPresent()){
            Healer healer = optionalHealer.get();
            healer = healerRepository.save(healer);
            return entityToDto(healer);
        }
        return null;
    }

    @Override
    public String register(SignupDTO dto) {
        if(healerRepository.findById(dto.getEmail()).isPresent()){
            return null;
        }
        Healer healer = dtoToEntity(dto);
        healer.setHealer_nickname(dto.getEmail());
        healer = healerRepository.save(healer);
        return healer.getHealer_email();
    }

    @Override
    public SignupDTO login(SignupDTO dto) {
        return null;
    }
}
