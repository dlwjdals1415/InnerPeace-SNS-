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

    List<PostDTO> list();

    List<PostDTO> scrollList(Long postNo);

    List<PostDTO> search(String searchkey);

    PostDTO detail(Long postNo, String healerNickName);

    PostDTO modify(PostDTO dto, String loginHealer);

    String like(Long postNo, String healerNickname);

    int deletePost(PostDTO postDTO);

    default Post dtoToEntity(PostDTO dto) {
        Post entity = Post.builder()
                .postImage(dto.getPost_image())
                .postContent(dto.getPost_content())
                .build();
        return entity;
    }

    default PostDTO entityToDto(Post entity) {
        PostDTO dto = PostDTO.builder()
                .post_no(entity.getPostNo())
                .post_map_lng(String.valueOf(entity.getPostMapLng()))
                .post_map_lat(String.valueOf(entity.getPostMapLat()))
                .post_content(entity.getPostContent())
                .post_image(entity.getPostImage())
                .tags(entity.getTags())
                .post_regday(LocalDateTime.from(entity.getReg_date()))
                .build();
        return dto;
    }

}
