package com.social.innerPeace.entity;

import com.social.innerPeace.ip_enum.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"followerList","followingList","postList"})
public class Healer extends BaseEntity{
    @Id
    @Column(name = "healer_email")
    private String healerEmail;

    @Column(length = 200,nullable = false)
    private String healer_pw;

    @Column(length = 50,nullable = false)
    private String healer_name;

    @Column(length = 20,nullable = false)
    private String healer_phone;

    @Column(nullable = false)
    @Builder.Default
    private boolean healer_status = true;

    private String haeler_profile_image;

    private String healer_randomcode;

    @Column(length = 30, nullable = false)
    private String healer_nickname;

    @Column(length = 1)
    private String healer_gender;

    private LocalDate healer_bitrh;

    @Column(length = 400)
    private String healer_statusmessage;

    private boolean ad_agree;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder.Default
    @OneToMany(mappedBy = "follower",cascade = CascadeType.ALL)
    private List<Follow> followerList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "following",cascade = CascadeType.ALL)
    private List<Follow> followingList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post_writer",cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "healer_no",cascade = CascadeType.ALL)
    private List<Post_Like> postLikeList = new ArrayList<>();
}
