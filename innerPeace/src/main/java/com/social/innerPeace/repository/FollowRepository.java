package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow,Long> {
    Optional<Follow> findByFollowerEmailAndFollowingEmail(String followerHealerEmail, String healerHealerEmail);

    List<Follow> findByFollowerEmail(String followerHealerEmail);
}
