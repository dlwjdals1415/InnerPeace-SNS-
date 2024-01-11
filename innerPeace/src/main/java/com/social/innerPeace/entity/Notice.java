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
    private Long notice_no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private Admin notice_writer;

    @Column(length = 50,nullable = false)
    private String notice_title;

    @Column(length = 2000,nullable = false)
    private String notice_content;
}
