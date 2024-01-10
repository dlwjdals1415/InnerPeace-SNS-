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
@ToString(exclude = {"board_writer","commentList","reportList"})
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long board_no;

    @Column(length = 50,nullable = false)
    private String board_title;

    @Column(length = 1500,nullable = false)
    private String board_content;

    private String board_image;

    private int map_point_x;

    private int map_point_y;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_nickname")
    private Healer board_writer;

    @Builder.Default
    @OneToMany(mappedBy = "board_no",cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "report_board",cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board_no",cascade = CascadeType.ALL)
    private List<Tag> tagList = new ArrayList<>();
}
