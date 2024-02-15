package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"answerNo","admin"})
public class Answer extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long answerNo;

    @Builder.Default
    @OneToOne(mappedBy = "answer")
    private Inquiry inquiryNo = new Inquiry();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adminId")
    private Admin admin;

    @Column(length = 2000,nullable = false)
    private String answerContent;
}
