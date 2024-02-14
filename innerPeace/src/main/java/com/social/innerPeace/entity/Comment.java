package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"post_no","healer_nickname","reportList"})
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comment_no;

    @Column(length = 200,nullable = false)
    private String comment_content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_no")
    private Post post_no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_nickname")
    private Healer healer;

    @Builder.Default
    @OneToMany(mappedBy = "report_comment",cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

}
