package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"healer","answer"})
public class Inquiry extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiryNo;

    @Column(length = 50,nullable = false)
    private String inquiryTitle;

    @Column(length = 2000,nullable = false)
    private String inquiryContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healerEmail")
    private Healer healer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "answerNo",referencedColumnName = "answerNo")
    private Answer answer;
}
