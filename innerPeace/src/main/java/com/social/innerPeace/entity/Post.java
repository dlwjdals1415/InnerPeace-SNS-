package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"healer","commentList","reportList"})
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @Column(length = 1500,nullable = false)
    private String postContent;

    @Column(length = 512)
    private String postImage;

    private float postMapLat;

    private float postMapLng;

    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "healerEmail")
    private Healer healer;

    @Builder.Default
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    private List<Post_Like> postLikeList = new ArrayList<>();

}
