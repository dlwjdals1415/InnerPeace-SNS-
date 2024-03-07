package com.social.innerPeace.detail.service;

import com.social.innerPeace.detail.MemberDetails;
import com.social.innerPeace.entity.Member;
import com.social.innerPeace.repository.MemberRepository;
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
    MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> healer = memberRepository.findById(username);
        if(healer.isEmpty()){
            throw new UsernameNotFoundException(username);
        }
        return new MemberDetails(healer.get());
    }
}
