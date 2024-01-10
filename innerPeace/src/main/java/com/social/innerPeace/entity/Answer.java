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
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inquiry_no")
    private Inquiry answer_no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_no")
    private Admin answer_writer;

    @Column(length = 2000,nullable = false)
    private String answer_content;
}
