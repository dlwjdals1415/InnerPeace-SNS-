package com.social.innerPeace.user.follow.service;

import org.springframework.stereotype.Service;

public interface HealerFollowService {
    String follow(String follower, String healerNickname);
}
