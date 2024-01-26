package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealerPageDTO {
    private String healer_id;
    private String healer_profile;
    private String healer_nickname;
    private String followstat;
    private int postcount;
    private int healer_follower;
    private int healer_follow;
    private String healer_statusmessage;
}
