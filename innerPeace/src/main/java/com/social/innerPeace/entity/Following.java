package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"following_id","healer_id"})
public class Following extends BaseEntity{
    @ManyToOne(fetch = FetchType.EAGER)
    @Id
    @JoinColumn(name = "healer_id")
    private Healer healer_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @Id
    @JoinColumn(name = "heaer_id")
    private Healer following_id;
}
