package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Healer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HealerRepository extends JpaRepository<Healer,String> {

    Optional<Healer> findByHealerNickName(String healerNickName);

    @Query("SELECT h.followerList FROM Healer h WHERE h.healerEmail = :healerEmail")
    List<Follow> findFollowersByHealerEmail(@Param("healerEmail") String healerEmail);

    @Query("SELECT h.followingList FROM Healer h WHERE h.healerEmail = :healerEmail")
    List<Follow> findFollowingByHealerEmail(@Param("healerEmail") String healerEmail);

}
