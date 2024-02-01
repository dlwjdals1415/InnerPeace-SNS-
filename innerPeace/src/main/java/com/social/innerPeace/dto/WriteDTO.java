package com.social.innerPeace.dto;

import com.social.innerPeace.entity.Healer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WriteDTO{
    private Long post_no;
    private Float post_map_lat;
    private Float post_map_lng;
    private String post_image_thumbnail;
    private String post_writer;
    private String post_content;
    private String post_tags;
    private MultipartFile post_image;
    private String db_post_image;

}
