package com.social.innerPeace.board;

import com.social.innerPeace.entity.BaseEntity;
import jakarta.persistence.*;

import java.lang.reflect.Member;

public class BoardHomeController extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
