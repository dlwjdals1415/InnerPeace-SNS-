package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDTO {
    private String email;
    private String pw;
    private String name;
    private String phone;
    private String birth;
    private String nickName;
    private String gender;
    private String profile_image;
    private String statusMessage;
    private List<String> ad_agree;
    private String follow_status;
    private Integer post_count;
    private Integer follower_count;
    private Integer follow_count;
}
