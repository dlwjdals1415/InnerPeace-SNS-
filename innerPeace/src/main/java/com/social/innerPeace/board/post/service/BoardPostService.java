package com.social.innerPeace.board.post.service;

import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.dto.WriteDTO;
import com.social.innerPeace.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public interface BoardPostService {

    String write(WriteDTO dto);


    default Post dtoToEntity(WriteDTO dto){
        Post entity = Post.builder()
                .tags(Collections.singletonList(dto.getPost_tags()))
                .post_writer(dto.getPost_writer())
                .post_image(String.valueOf(dto.getPost_image()))
                .post_content(dto.getPost_content())
                .map_point_lng(dto.getPost_map_lng())
                .map_point_lat(dto.getPost_map_lat())
                .build();
        return entity;
    }

    default PostDTO entityToDto(Post entity){
        PostDTO dto = PostDTO.builder()
                .map_point_lng(entity.getMap_point_lng())
                .map_point_lat(entity.getMap_point_lat())
                .post_content(entity.getPost_content())
                .post_image(entity.getPost_image())
                .tags(entity.getTags())
                .post_writer(entity.getPost_writer())
                .build();
        return dto;
    }
}
