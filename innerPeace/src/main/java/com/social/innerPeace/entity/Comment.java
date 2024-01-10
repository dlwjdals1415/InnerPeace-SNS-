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
@ToString(exclude = {"board_no","healer_nickname","reportList"})
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_no;

    @Column(length = 200,nullable = false)
    private String comment_content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_no")
    private Board board_no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_nickname")
    private Healer healer_nickname;

    @Builder.Default
    @OneToMany(mappedBy = "report_comment",cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

}
