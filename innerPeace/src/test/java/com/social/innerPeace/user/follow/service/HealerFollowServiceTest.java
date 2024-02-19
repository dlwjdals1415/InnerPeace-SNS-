package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.HealerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class HealerFollowServiceTest {
    @Autowired
    private  HealerFollowService healerFollowService;
    @Autowired
    private HealerRepository healerRepository;

    @Test
    void followTest() {
        List<Healer> healerList = healerRepository.findAll();
        healerList.forEach(healer -> {
            healerList.forEach(healer1 -> {
                if(healer.getHealerEmail().equals(healer1.getHealerEmail())){
                    return;
                }
                log.info(healerFollowService.follow(healer.getHealerNickName(), healer1.getHealerNickName()));
            });
        });
    }
}
