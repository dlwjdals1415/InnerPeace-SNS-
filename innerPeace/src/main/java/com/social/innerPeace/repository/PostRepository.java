package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

}
