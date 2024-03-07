package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.entity.Member;
import com.social.innerPeace.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class UserFollowServiceTest {
    @Autowired
    private UserFollowService userFollowService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void followTest() {
        List<Member> memberList = memberRepository.findAll();
        memberList.forEach(member -> {
            memberList.forEach(member1 -> {
                if(member.getEmail().equals(member1.getEmail())){
                    return;
                }
                log.info(userFollowService.follow(member.getNickName(), member1.getNickName()));
            });
        });
    }
}
