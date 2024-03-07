package com.social.innerPeace.user.account.service;

import com.social.innerPeace.dto.MemberDTO;
import com.social.innerPeace.ip_enum.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class UserAccountServiceTest {

    @Autowired
    private UserAccountService userAccountService;

    @Test
    void registTest(){
        IntStream.rangeClosed(1, 100).forEach(i -> {
            MemberDTO memberDTO = MemberDTO.builder()
                    .email("user" + i + "@innerpeace.com")
                    .pw("1111")
                    .name("user" + i)
                    .phone("010-1111-1111")
                    .birth("1990-01-01")
                    .build();
            userAccountService.register(memberDTO, Role.ROLE_USER);
        });
    }

}
