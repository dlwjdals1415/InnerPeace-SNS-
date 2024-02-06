package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment c where c.post_no.postNo = :postNo")
    List<Comment>findAllByPostNo(@Param("postNo") Long postNo);


}
