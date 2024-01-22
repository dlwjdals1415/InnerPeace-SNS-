package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"inquiry_writer","answer"})
public class Inquiry extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inquiry_no;

    @Column(length = 50,nullable = false)
    private String inquiry_title;

    @Column(length = 2000,nullable = false)
    private String inquiry_content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_email")
    private Healer inquiry_writer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "answer_no",referencedColumnName = "answer_no")
    private Answer answer;
}
