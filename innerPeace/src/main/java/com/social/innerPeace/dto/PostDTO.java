package com.social.innerPeace.dto;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post_Like;
import com.social.innerPeace.entity.Report;
import jakarta.persistence.*;
import com.social.innerPeace.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long post_no;
    private String post_content;
    private String post_image;
    private float map_point_lat;
    private float map_point_lng;
    private List<String> tags;
    private String healer_id;
    private Healer post_writer;
    private String followstat;
    private int likes;
    private String likeposition;
    private String profileImg;
    private LocalDateTime post_regday;


    public static PostDTO toPostDTO(Post post){
        PostDTO postDTO = new PostDTO();
        postDTO.setPost_no(post.getPost_no());
        postDTO.setPost_content(post.getPost_content());
        postDTO.setPost_image(post.getPost_image());
        postDTO.setMap_point_lat(post.getMap_point_lat());
        postDTO.setMap_point_lng(post.getMap_point_lng());
        postDTO.setTags(post.getTags());
        postDTO.setPost_writer(post.getPost_writer());
        postDTO.setProfileImg(post.getPost_image());
        postDTO.setPost_regday(post.getReg_date());
        return postDTO;
    }


}

