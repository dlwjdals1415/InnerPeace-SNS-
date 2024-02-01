package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailDTO {
    private Long post_no;
    private String post_content;
    private String post_image;
    private String healer_id;
    private String post_writer;
    private String profileImg;
    private String followstat;
    private String likeposition;
    private List<String> tags;
    private float map_point_lat;
    private float map_point_lng;
    private int likes;
    private int likecount;
    private int commentcount;
    private int likestat;
    private LocalDate post_regday;
}
