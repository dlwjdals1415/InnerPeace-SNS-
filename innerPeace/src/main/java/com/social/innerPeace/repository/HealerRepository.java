package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Healer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HealerRepository extends JpaRepository<Healer,String> {
    Optional<Healer> findByHealerNickName(String healerNickName);
}
