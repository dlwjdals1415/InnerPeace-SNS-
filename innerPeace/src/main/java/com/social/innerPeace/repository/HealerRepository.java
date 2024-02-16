package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Query("SELECT h.postList FROM Healer h WHERE h.healerEmail = :healerEmail")
    List<Post> findPostByHealerEmail(@Param("healerEmail") String healerEmail);

    @Query("SELECT f FROM Follow f WHERE f.following = :following ORDER BY f.followNo DESC")
    Page<Follow> findByFollower(@Param("following") Healer following, Pageable pageable);

}
