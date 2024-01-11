package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
}
