package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Follow;
import com.social.innerPeace.entity.Member;
import com.social.innerPeace.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,String> {

    Optional<Member> findByNickName(String nickName);

    @Query("SELECT h.followerList FROM Member h WHERE h.email = :email")
    List<Follow> findFollowersByEmail(@Param("email") String email);

    @Query("SELECT h.followingList FROM Member h WHERE h.email = :email")
    List<Follow> findFollowingByEmail(@Param("email") String email);

    @Query("SELECT h.postList FROM Member h WHERE h.email = :email")
    List<Post> findPostByEmail(@Param("email") String email);

    @Query("SELECT f FROM Follow f WHERE f.following.email = :email ORDER BY f.followNo DESC")
    List<Follow> findByFollowingEmail(@Param("email") String Email, Pageable pageable);

    @Query("SELECT f FROM Follow f WHERE f.following.email = :email AND f.followNo <     :followNo ORDER BY f.followNo DESC")
    List<Follow> findByFollowingEmailAndFollowNoLessThanEqual(@Param("email") String email, @Param("followNo") Long followNo, Pageable pageable);

    @Query("SELECT f FROM Follow f WHERE f.follower.email = :email ORDER BY f.followNo DESC")
    List<Follow> findByFollowerEmail(@Param("email") String email, Pageable pageable);

    @Query("SELECT f FROM Follow f WHERE f.follower.email = :email AND f.followNo < :followNo ORDER BY f.followNo DESC")
    List<Follow> findByFollowerEmailAndFollowNoLessThanEqual(@Param("email") String email, @Param("followNo") Long followNo, Pageable pageable);
}
