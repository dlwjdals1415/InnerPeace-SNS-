package com.social.innerPeace.dto;

import com.social.innerPeace.entity.Healer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDTO {
    private String follow;
    private String followstatus;
    private Long follow_no;
    private String healer_nickname;
    private String healer_profile_image;
}
