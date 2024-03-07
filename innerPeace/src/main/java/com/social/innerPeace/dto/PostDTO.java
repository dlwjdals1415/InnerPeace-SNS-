package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile post_image_file;
    private String post_image;
    private String post_image_thumbnail;
    private String post_map_lat;
    private String post_map_lng;
    private List<String> tags;
    private String post_tags;
    private String profile_image;
    private String nickName;
    private String likeStatus;
    private LocalDateTime post_regday;
    private String followStatus;
    private int likes;
    private List<CommentDTO> commentList = new ArrayList<>();
}
