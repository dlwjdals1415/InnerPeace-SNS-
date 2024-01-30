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
    private float post_map_lat;
    private float post_map_lng;
    private String post_image_thumbnail;
    private String folderPath;
    private Healer post_writer;
    private String post_content;
    private String post_tags;
    private MultipartFile post_image;

}
