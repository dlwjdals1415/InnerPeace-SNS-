package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.member WHERE p.postNo = :postNo")
    Post findByPostNoWithMember(@Param("postNo") Long postNo);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t LIKE %:tag% ORDER BY p.postNo DESC")
    Page<Post> findByTagContaining(@Param("tag") String tag, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t LIKE %:tag% AND p.postNo < :postNo ORDER BY p.postNo DESC")
    Page<Post> findByTagContainingAndPostNoLessThan(@Param("tag") String tag, @Param("postNo") Long postNo, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.member.nickName LIKE %:NickName% ORDER BY p.postNo DESC")
    Page<Post> findByNickNameContaining(@Param("NickName") String NickName, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.member.nickName LIKE %:NickName% AND p.postNo < :postNo ORDER BY p.postNo DESC")
    Page<Post> findByNickNameContainingAndPostNoLessThan(@Param("NickName") String NickName, @Param("postNo") Long postNo, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postContent LIKE %:content% ORDER BY p.postNo DESC")
    Page<Post> findByContentContaining(@Param("content") String content, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.member.nickName = ?1 ORDER BY p.postNo DESC")
    List<Post> findByNickname(String Nickname, Sort postNo);

    @Query("SELECT p FROM Post p WHERE p.postContent LIKE %:content% AND p.postNo < :postNo ORDER BY p.postNo DESC")
    Page<Post> findByContentContainingAndPostNoLessThan(@Param("content") String content, @Param("postNo") Long postNo, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.postNo < :postNo ORDER BY p.postNo DESC")
    Page<Post> findByPostNoLessThanOrderByPostNoDesc(@Param("postNo") Long postNo, Pageable pageable);
}

