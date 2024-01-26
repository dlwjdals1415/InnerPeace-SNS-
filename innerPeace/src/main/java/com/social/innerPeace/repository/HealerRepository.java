package com.social.innerPeace.repository;

import com.social.innerPeace.entity.Healer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealerRepository extends JpaRepository<Healer,String> {
    Healer findByHealerEmail(String email);
}
