package com.social.innerPeace.entity;

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
}
