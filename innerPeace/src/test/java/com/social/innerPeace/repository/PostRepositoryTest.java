package com.social.innerPeace.repository;

import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
import com.social.innerPeace.ip_enum.Role;
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
    private HealerRepository healerRepository;

    @Test
    void insert(){
        List<Healer> healerList = healerRepository.findAll();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            healerList.forEach(healer -> {
                Post post = Post.builder()
                        .postContent("test" + i)
                        .postImage("test" + i+".jpg")
                        .healer(healer)
                        .tags(List.of(healer.getHealerName() + i,healer.getHealerNickName() + i))
                        .postMapLat(35.8999F)
                        .postMapLng(128.847F)
                        .build();
                postRepository.save(post);
            });
        });
    }
}
