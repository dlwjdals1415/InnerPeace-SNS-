package com.social.innerPeace.detail.service;

import com.social.innerPeace.detail.HealerDetails;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.HealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    @Autowired
    HealerRepository healerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Healer> healer = healerRepository.findById(username);
        if(healer.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        return new HealerDetails(healer.get());
    }
}
