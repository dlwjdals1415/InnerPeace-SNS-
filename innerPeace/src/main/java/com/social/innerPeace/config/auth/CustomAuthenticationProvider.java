package com.social.innerPeace.config.auth;

import com.social.innerPeace.entity.Healer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomLoadUserByUsername customLoadUserByUsername;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Healer healer = (Healer) customLoadUserByUsername.loadUserByUsername(authentication.getName().toString());
        String reqPassword = authentication.getCredentials().toString();
        if(!passwordEncoder.matches(reqPassword, healer.getHealer_pw())) throw new BadCredentialsException("Not Found User");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(healer.getRole().getKey()));
        return new UsernamePasswordAuthenticationToken(healer, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

}