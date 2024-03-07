package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.FollowDTO;

import java.util.List;

public interface UserFollowService {
    String follow(String follower, String healerNickname);

    List<FollowDTO> findFollowing(String healerNickname, String loginedMember);

    List<FollowDTO> findFollower(String healerNickname, String loginedMember);

    List<FollowDTO> findFollowingScroll(String healerNickname, String loginedMember, long followNo);

    List<FollowDTO> findFollowerScroll(String healerNickname, String loginedMember, long followNo);
}
