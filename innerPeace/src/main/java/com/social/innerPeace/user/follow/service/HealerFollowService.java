package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.FollowDTO;
import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Healer;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public interface HealerFollowService {
    String follow(String follower, String healerNickname);

    List<FollowDTO> findFollowing(String healerNickname);
}
