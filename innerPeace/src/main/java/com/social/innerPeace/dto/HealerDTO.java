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
public class HealerDTO {
    private String healer_email;
    private String healer_pw;
    private String healer_name;
    private String healer_phone;
    private String healer_birth;
    private String healer_nickname;
    private String healer_gender;
    private String haeler_profile_image;
    private String healer_statusmessage;
    private List<String> ad_agree;
    private String follow_status;
    private Integer post_count;
    private Integer healer_follower_count;
    private Integer healer_follow_count;
}
