package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Post_Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Post_Like,Long>{
    List<Post_Like> findByPostPostNo(Long postNo);
    Optional<Post_Like> findByHealerHealerEmailAndPostPostNo(String healerEmail, Long postNo);
}
