package com.social.innerPeace.dto;

import com.social.innerPeace.entity.Healer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    private String map_point_lat;
    private String map_point_lng;
    private List<String> tags;
    private String post_tags;
    private Healer post_writer;
    private String likeStatus;
    private LocalDate post_regday;
}
