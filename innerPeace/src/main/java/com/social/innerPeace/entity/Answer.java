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

    @Builder.Default
    @OneToOne(mappedBy = "answer")
    private Inquiry inquiry_no = new Inquiry();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_no")
    private Admin answer_writer;

    @Column(length = 2000,nullable = false)
    private String answer_content;
}
