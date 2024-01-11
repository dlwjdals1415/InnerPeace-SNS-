package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"answer_no","answer_writer"})
public class Answer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answer_no;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inquiry_no")
    private Inquiry inquiry_no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_no")
    private Admin answer_writer;

    @Column(length = 2000,nullable = false)
    private String answer_content;
}
