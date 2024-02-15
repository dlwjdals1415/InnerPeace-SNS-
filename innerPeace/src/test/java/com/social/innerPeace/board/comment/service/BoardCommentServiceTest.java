package com.social.innerPeace.board.comment.service;

import com.social.innerPeace.dto.CommentDTO;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@Slf4j
public class BoardCommentServiceTest {

    @Autowired
    private BoardCommentService boardCommentService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private HealerRepository healerRepository;

    @Test
    void writeTest(){
        List<Healer> healerList = healerRepository.findAll();
        List<Post> postList = postRepository.findAll();

        postList.forEach(post -> {
            healerList.forEach(healer -> {
                CommentDTO commentDTO = CommentDTO.builder()
                        .post_no(post.getPostNo())
                        .nickName(healer.getHealerNickName())
                        .comment_content("hello my name is " + healer.getHealerName() + " and I'm healer")
                        .build();
                boardCommentService.write(commentDTO);
            });
        });
    }
}
