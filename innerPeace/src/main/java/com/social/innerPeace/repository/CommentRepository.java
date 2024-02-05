package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.naming.Name;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    @Query("select c from Comment as c where c.post_no = :post_no")
    List<Comment>findAllByPostID(@Param("post_no") Long postId);
}
