package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Member;
import com.social.innerPeace.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void insert() {
        List<Member> memberList = memberRepository.findAll();
        memberList.forEach(healer -> {
            IntStream.rangeClosed(1, 10).forEach(i -> {
                Post post = Post.builder()
                        .postContent("test" + i)
                        .postImage("test" + i + ".jpg")
                        .member(healer)
                        .tags(List.of("#" + healer.getName() + i, "#" + healer.getNickName() + i))
                        .postMapLat(35.8999F)
                        .postMapLng(128.847F)
                        .build();
                postRepository.save(post);
            });
        });
    }
}
