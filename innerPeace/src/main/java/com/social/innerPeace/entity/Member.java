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
public class Member extends BaseEntity{
    @Id
    @Column(name = "healerEmail")
    private String email;

    private String pw;

    @Column(length = 50,nullable = false)
    private String name;

    @Column(length = 20,nullable = false)
    private String phone;

    @Column(nullable = false)
    @Builder.Default
    private boolean status = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean emailVerified = true;

    private String profileImage;

    @Column(length = 30, nullable = false, name = "nickName", unique = true)
    private String nickName;

    @Column(length = 8)
    private String gender;

    private LocalDate birth;

    @Column(length = 400)
    private String StatusMessage;

    @Builder.Default
    private boolean adAgree = false;

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
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL)
    private List<Post_Like> postLikeList = new ArrayList<>();
}
