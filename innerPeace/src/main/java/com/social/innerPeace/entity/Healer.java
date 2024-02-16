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
    @Column(name = "healerEmail")
    private String healerEmail;

    private String healerPw;

    @Column(length = 50,nullable = false)
    private String healerName;

    @Column(length = 20,nullable = false)
    private String healerPhone;

    @Column(nullable = false)
    @Builder.Default
    private boolean healerStatus = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean emailVerified = true;

    private String haelerProfileImage;

    private String healerRandomcode;

    @Column(length = 30, nullable = false, name = "healerNickName", unique = true)
    private String healerNickName;

    @Column(length = 1)
    private String healerGender;

    private LocalDate healerBitrh;

    @Column(length = 400)
    private String healerStatusmessage;

    private boolean adAgree;

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
    @OneToMany(mappedBy = "healer",cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "healer",cascade = CascadeType.ALL)
    private List<Post_Like> postLikeList = new ArrayList<>();
}
