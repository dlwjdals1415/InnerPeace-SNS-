package com.social.innerPeace.config.auth;

import com.social.innerPeace.dto.SignupDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.HealerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLoadUserByUsername{


    private final HealerRepository healerRepository;
    private final HttpSession session;
    public Healer loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Healer> healer = healerRepository.findById(email);
        if(healer == null || healer.isEmpty())     throw new UsernameNotFoundException("Not Found User");
        session.setAttribute("user",new SignupDTO());
        return healer.get();
    }

}