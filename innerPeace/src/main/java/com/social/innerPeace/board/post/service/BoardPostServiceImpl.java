package com.social.innerPeace.board.post.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.dto.WriteDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardPostServiceImpl implements BoardPostService{
    @Autowired
    PostRepository postRepository;
    @Autowired
    HealerRepository healerRepository;
    @Autowired
    FileStore fileStore;
    @Override
    public String write(WriteDTO dto) {
        Optional<Healer> optionalHealer = healerRepository.findById(dto.getPost_writer());
        if(optionalHealer.isPresent()){
            Post post;
            try {
                dto = fileStore.storeFile(dto);
                Healer healer =optionalHealer.get();
                post = dtoToEntity(dto);
                post.setPost_writer(healer);
                post = postRepository.save(post);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return post.getPost_image();
        }
        return null;
    }
}
