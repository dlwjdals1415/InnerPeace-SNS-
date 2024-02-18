package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adminId")
    private Admin admin;

    @Column(length = 50,nullable = false)
    private String noticeTitle;

    @Column(length = 2000,nullable = false)
    private String noticeContent;
}
