package com.social.innerPeace.dto;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post_Like;
import com.social.innerPeace.entity.Report;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
    private Long postNo;
    private String post_content;
    private String post_image;
    private float map_point_lat;
    private float map_point_lng;
    private List<String> tags;
    private Healer post_writer;
    private List<Comment> commentList = new ArrayList<>();
    private List<Report> reportList = new ArrayList<>();
    private List<Post_Like> postLikeList = new ArrayList<>();
}
