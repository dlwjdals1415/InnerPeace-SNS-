package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Long post_no;

    @Column(length = 50,nullable = false)
    private String post_title;

    @Column(length = 1500,nullable = false)
    private String post_content;

    private String post_image;

    private int map_point_x;

    private int map_point_y;

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
    private List<Tag> tagList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post_no",cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();
}
