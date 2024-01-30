package com.social.innerPeace.board.post.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.dto.WriteDTO;
import com.social.innerPeace.entity.Post;
import com.social.innerPeace.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BoardPostServiceImpl implements BoardPostService{
    @Autowired
    PostRepository postRepository;
    @Autowired
    FileStore fileStore;
    @Override
    public String write(WriteDTO dto) {
        Post post = dtoToEntity(dto);
        try {
            fileStore.storeFile(dto);
            post = postRepository.save(post);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return post.getPost_image();
    }
}
