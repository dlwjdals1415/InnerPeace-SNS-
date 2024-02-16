package com.social.innerPeace.user.follow.service;

import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Follow;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public interface HealerFollowService {
    String follow(String follower, String healerNickname);

    List<HealerDTO> findAll(String healerNickname);

    default HealerDTO entityToDto(Follow follow){
        HealerDTO healerDTO = HealerDTO.builder()
                .healer_nickname(follow.getFollowing().getHealerNickName())
                .build();
        return healerDTO;
    }

    default List<HealerDTO> toList(Page<Follow> healerList){
        return healerList.stream().map(entity->entityToDto(entity)).collect(Collectors.toList());
    }
}
