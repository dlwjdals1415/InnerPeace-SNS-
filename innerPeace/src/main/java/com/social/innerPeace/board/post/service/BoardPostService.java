package com.social.innerPeace.board.post.service;

import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.entity.Post;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public interface BoardPostService {

    String write(PostDTO dto);

    PostDTO findImagename(Long post_no);

    List<PostDTO> findAllPostsWithBase64Thumbnail();

    PostDTO findByPostNo(Long postNo);

    default Post dtoToEntity(PostDTO dto){
        Post entity = Post.builder()
                .tags(Collections.singletonList(dto.getPost_tags()))
                .post_image(dto.getPost_image())
                .post_content(dto.getPost_content())
                .build();
        return entity;
    }

    default PostDTO entityToDto(Post entity){
        PostDTO dto = PostDTO.builder()
                .post_no(entity.getPostNo())
                .map_point_lng(String.valueOf(entity.getMap_point_lng()))
                .map_point_lat(String.valueOf(entity.getMap_point_lat()))
                .post_content(entity.getPost_content())
                .post_image(entity.getPost_image())
                .tags(entity.getTags())
                .post_writer(entity.getPost_writer().getHealer_nickname())
                .post_regday(LocalDate.from(entity.getReg_date()))
                .build();
        return dto;
    }
}
