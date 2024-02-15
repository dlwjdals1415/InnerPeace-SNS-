package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByFollowerHealerEmailAndFollowingHealerEmail(String followerHealerEmail, String healerHealerEmail);
}
