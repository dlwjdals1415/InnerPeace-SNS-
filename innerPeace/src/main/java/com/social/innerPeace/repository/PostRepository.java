package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.healer WHERE p.postNo = :postNo")
    Post findByPostNoWithHealer(@Param("postNo") Long postNo);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t LIKE %:tag% ORDER BY p.postNo DESC")
    Page<Post> findByTagContaining(@Param("tag") String tag, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.healer.healerNickName LIKE %:healerNickName% ORDER BY p.postNo DESC")
    Page<Post> findByHealerNickNameContaining(@Param("healerNickName") String healerNickName, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postContent LIKE %:content% ORDER BY p.postNo DESC")
    Page<Post> findByContentContaining(@Param("content") String content, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postNo < :postNo ORDER BY p.postNo DESC")
    Page<Post> findByPostNoLessThanOrderByPostNoDesc(@Param("postNo") Long postNo, Pageable pageable);
}

