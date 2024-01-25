package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowDTO {
    private String healer_id;
    private String healer_nickname;
    private String healer_profile;
    private String followstas;
}
