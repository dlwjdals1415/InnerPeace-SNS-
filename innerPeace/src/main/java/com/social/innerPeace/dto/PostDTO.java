package com.social.innerPeace.dto;

import com.social.innerPeace.entity.Healer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long post_no;
    private String post_title;
    private String post_content;
    private String post_image;
    private float map_point_lat;
    private float map_point_lng;
    private List<String> tags;
    private String post_writer;
    private String follow;
    private int likes;
    private String likeposition;
    private String profileImg;
    private LocalDateTime post_regday;
}
