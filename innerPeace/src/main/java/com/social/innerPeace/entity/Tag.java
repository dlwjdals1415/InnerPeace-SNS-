package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"post_no"})
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tag_no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_no")
    private Post post_no;

    @Column(length = 20,nullable = false)
    private String tag_content;
}
