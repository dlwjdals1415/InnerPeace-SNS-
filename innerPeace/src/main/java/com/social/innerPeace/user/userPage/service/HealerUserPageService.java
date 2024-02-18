package com.social.innerPeace.user.userPage.service;

import com.social.innerPeace.dto.HealerDTO;
import com.social.innerPeace.entity.Healer;

public interface HealerUserPageService {
    HealerDTO findHealer(String healerNickname, String loginedHealer);


    default HealerDTO entityToDto(Healer healer){
        HealerDTO dto = HealerDTO.builder()
                .healer_nickname(healer.getHealerNickName())
                .healer_statusmessage(healer.getHealerStatusmessage())
                .build();
        return dto;
    }

}
