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
    @JoinColumn(name = "healer_id")
    private Healer inquiry_writer;

    @Builder.Default
    @OneToOne(mappedBy = "inquiry_no",cascade = CascadeType.ALL)
    private Answer answer = new Answer();
}
