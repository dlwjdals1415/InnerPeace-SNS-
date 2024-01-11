package com.social.innerPeace.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"followerList","followingList","boardList"})
public class Healer extends BaseEntity{
    @Id
    @Column(length = 20)
    private String healer_id;

    @Column(length = 30,nullable = false)
    private String healer_pw;

    @Column(length = 50,nullable = false)
    private String healer_name;

    @Column(length = 100,nullable = false)
    private String healer_email;

    @Column(length = 20,nullable = false)
    private String healer_phone;

    @Column(nullable = false)
    @Builder.Default
    private boolean healer_status = true;

    @Column(length = 50)
    private String healer_randomcode;

    @Column(length = 30, nullable = false)
    private String healer_nickname;

    @Column(length = 1)
    private String healer_gender;

    private LocalDateTime healer_bitrh;

    @Column(length = 400)
    private String healer_statusmessage;

    @Builder.Default
    @OneToMany(mappedBy = "follower",cascade = CascadeType.ALL)
    private List<Follow> followerList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "following",cascade = CascadeType.ALL)
    private List<Follow> followingList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "board_writer",cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();
}
