package com.social.innerPeace.user.account.service;

import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.dto.PostDTO;
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
            HealerDTO healerDTO = HealerDTO.builder()
                    .healer_email("user" + i + "@innerpeace.com")
                    .healer_pw("1111")
                    .healer_name("user" + i)
                    .healer_phone("010-1111-1111")
                    .healer_birth("1990-01-01")
                    .build();
            userAccountService.register(healerDTO, Role.ROLE_USER);
        });
    }

}
