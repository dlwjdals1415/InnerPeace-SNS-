package com.social.innerPeace.board.post.service;

import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public interface BoardPostService {

    String write(PostDTO dto);

    PostDTO findImagename(Long post_no);

    List<PostDTO> findAllPostsWithBase64Thumbnail();

    PostDTO findByPostNo(Long postNo);

    String modify(PostDTO dto);

    default Post dtoToEntity(PostDTO dto){
        Post entity = Post.builder()
                .post_image(dto.getPost_image())
                .post_content(dto.getPost_content())
                .build();
        return entity;
    }

    default PostDTO entityToDto(Post entity){
        PostDTO dto = PostDTO.builder()
                .post_no(entity.getPostNo())
                .post_map_lng(String.valueOf(entity.getPost_map_lng()))
                .post_map_lat(String.valueOf(entity.getPost_map_lat()))
                .post_content(entity.getPost_content())
                .post_image(entity.getPost_image())
                .tags(entity.getTags())
                .post_regday(LocalDateTime.from(entity.getReg_date()))
                .build();
        return dto;
    }
}
