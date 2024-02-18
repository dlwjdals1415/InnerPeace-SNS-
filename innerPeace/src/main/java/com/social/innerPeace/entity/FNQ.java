package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "admin")
public class FNQ extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fnqNo;

    @Column(length = 100,nullable = false)
    private String fnqTitle;

    @Column(length = 2000,nullable = false)
    private String fnqContent;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adminId")
    private Admin admin;

}
