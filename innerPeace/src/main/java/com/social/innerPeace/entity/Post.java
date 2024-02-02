package com.social.innerPeace.entity;

import com.social.innerPeace.dto.PostDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"post_writer","commentList","reportList"})
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name = "post_no")
    private Long postNo;

    @Column(length = 1500,nullable = false)
    private String post_content;

    private String post_image;

    private float map_point_lat;

    private float map_point_lng;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "healer_nickname")
    private Healer post_writer;

    @Builder.Default
    @OneToMany(mappedBy = "post_no",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "report_post",cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post_no",cascade = CascadeType.ALL)
    private List<Post_Like> postLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "post_no", fetch = FetchType.LAZY)
    private List<Comment> commentEntityList = new ArrayList<>();

    public static Post toSaveEntity(PostDTO postDTO){
        Post post = new Post();
        post.setPost_no(post.getPost_no());
        post.setPost_content(post.getPost_content());
        post.setPost_image(post.getPost_image());
        post.setMap_point_lat(post.getMap_point_lat());
        post.setMap_point_lng(post.getMap_point_lng());
        post.setTags(post.getTags());
        post.setPost_writer(post.getPost_writer());
        post.setCommentList(post.getCommentList());
        post.setReportList(post.getReportList());
        post.setPostLikeList(post.getPostLikeList());
        post.setCommentEntityList(post.getCommentEntityList());


        return post;
    }
    public static Post toUpdateEntity(PostDTO postDTO){
        Post post = new Post();
        post.setPost_no(postDTO.getPost_no());
        post.setPost_content(postDTO.getPost_content());
        post.setPost_image(postDTO.getPost_image());
        post.setMap_point_lng(postDTO.getMap_point_lng());
        post.setMap_point_lat(postDTO.getMap_point_lat());
        post.setTags(post.getTags());
        post.setPost_writer(post.getPost_writer());
        post.setCommentList(post.getCommentList());
        post.setReportList(post.getReportList());
        post.setPostLikeList(post.getPostLikeList());
        post.setCommentEntityList(post.getCommentEntityList());
       return  post;
    }
    public static Post toSaveFileEntity(PostDTO postDTO){
        Post post = new Post();
        post.setPost_no(postDTO.getPost_no());
        post.setPost_content(postDTO.getPost_content());
        post.setPost_image(postDTO.getPost_image());
        post.setMap_point_lng(postDTO.getMap_point_lng());
        post.setMap_point_lat(postDTO.getMap_point_lat());
        post.setTags(post.getTags());
        post.setPost_writer(post.getPost_writer());
        post.setCommentList(post.getCommentList());
        post.setReportList(post.getReportList());
        post.setPostLikeList(post.getPostLikeList());
        post.setCommentEntityList(post.getCommentEntityList());
       return  post;
    }
}
