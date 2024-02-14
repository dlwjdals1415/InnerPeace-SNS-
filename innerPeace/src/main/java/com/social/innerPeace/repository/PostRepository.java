package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT p FROM Post p JOIN FETCH p.healer WHERE p.postNo = :postNo")
    Post findByPostNoWithHealer(@Param("postNo") Long postNo);


}
