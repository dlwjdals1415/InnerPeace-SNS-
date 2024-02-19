package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.FollowDTO;

import java.util.List;

public interface HealerFollowService {
    String follow(String follower, String healerNickname);

    List<FollowDTO> findFollowing(String healerNickname, String loginedHealer);

    List<FollowDTO> findFollower(String healerNickname, String loginedHealer);

    List<FollowDTO> findFollowingScroll(String healerNickname, String loginedHealer, long followNo);

    List<FollowDTO> findFollowerScroll(String healerNickname, String loginedHealer, long followNo);
}
