package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"post_no","healer_no"})
public class Like extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_no")
    private Post post_no;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "healer_no")
    private Healer healer_no;
}
