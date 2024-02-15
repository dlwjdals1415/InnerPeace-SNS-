package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"healer","post","comment"})
public class Report extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healerEmail")
    private Healer healer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "postNo")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "commentNo")
    private Comment comment;

    @Column(length = 100,nullable = false)
    private String reportReason;

    @Builder.Default
    private boolean reportStatus = false;
}
