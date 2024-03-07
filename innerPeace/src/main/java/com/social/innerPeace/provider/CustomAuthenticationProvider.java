package com.social.innerPeace.provider;

import com.social.innerPeace.detail.MemberDetails;
import com.social.innerPeace.detail.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = authentication.getName();
        String password = (String) authentication.getCredentials();



        MemberDetails memberDetails = (MemberDetails) userService.loadUserByUsername(userEmail);

        String dbPassword = memberDetails.getPassword();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(!passwordEncoder.matches(password,dbPassword)){
            throw new BadCredentialsException("아이디 또는 비밀번호가 일치하지 않습니다");
        }

        if(!memberDetails.isEnabled()){
            throw new BadCredentialsException("이메일 인증을 완료해주세요");
        }

        if(!memberDetails.isAccountNonLocked()){
            throw new BadCredentialsException("계정이 잠겼습니다. 관리자에게 문의해주세요");
        }

        return new UsernamePasswordAuthenticationToken(memberDetails,null, memberDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}