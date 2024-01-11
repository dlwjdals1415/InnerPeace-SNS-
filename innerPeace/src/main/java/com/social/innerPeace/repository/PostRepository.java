package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
