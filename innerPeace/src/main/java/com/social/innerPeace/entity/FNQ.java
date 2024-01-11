package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "fnq_writer")
public class FNQ extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fnq_no;

    @Column(length = 100,nullable = false)
    private String fnq_title;

    @Column(length = 2000,nullable = false)
    private String fnq_content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id")
    private Admin fnq_writer;

}
