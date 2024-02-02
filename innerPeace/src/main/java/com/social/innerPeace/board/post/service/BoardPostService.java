package com.social.innerPeace.board.post.service;

import com.social.innerPeace.dto.PosTListDTO;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.dto.WriteDTO;
import com.social.innerPeace.entity.Post;

import java.util.Collections;
import java.util.List;

public interface BoardPostService {

    String write(WriteDTO dto);

    PosTListDTO findImagename(Long post_no);

    List<PosTListDTO> findAllPostsWithBase64Thumbnail();

    PosTListDTO findByPostNo(Long postNo);

    default Post dtoToEntity(WriteDTO dto){
        Post entity = Post.builder()
                .tags(Collections.singletonList(dto.getPost_tags()))
                .post_image(dto.getDb_post_image())
                .post_content(dto.getPost_content())
                .map_point_lng(dto.getPost_map_lng())
                .map_point_lat(dto.getPost_map_lat())
                .build();
        return entity;
    }

    default PosTListDTO entityToDto(Post entity){
        PosTListDTO dto = PosTListDTO.builder()
                .post_no(entity.getPostNo())
                .map_point_lng(entity.getMap_point_lng())
                .map_point_lat(entity.getMap_point_lat())
                .post_content(entity.getPost_content())
                .post_image(entity.getPost_image())
                .tags(entity.getTags())
                .post_writer(String.valueOf(entity.getPost_writer()))
                .build();
        return dto;
    }
}
