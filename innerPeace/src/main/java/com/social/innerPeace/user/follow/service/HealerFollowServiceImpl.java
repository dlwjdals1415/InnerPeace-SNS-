package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.FollowDTO;
import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.repository.FollowRepository;
import com.social.innerPeace.repository.HealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HealerFollowServiceImpl implements HealerFollowService{
    @Autowired
    private HealerRepository healerRepository;
    @Autowired
    private FollowRepository followRepository;

    @Override
    public String follow(String follower, String healerNickname) {
        Optional<Healer> follow = healerRepository.findByHealerNickName(follower);
        if (follow.isEmpty()){
            return null;
        }
        Optional<Healer> healer = healerRepository.findByHealerNickName(healerNickname);
        if (healer.isEmpty()){
            return null;
        }
        Optional<Follow> followOptional = followRepository.findByFollowerHealerEmailAndFollowingHealerEmail(healer.get().getHealerEmail(),follow.get().getHealerEmail());
        if(followOptional.isPresent()){
            followRepository.delete(followOptional.get());
            return "팔로우";
        }else{
            Follow doFollow = Follow.builder()
                    .follower(follow.get())
                    .following(healer.get())
                    .build();
            followRepository.save(doFollow);
            return "팔로우 취소";
        }
    }
}
