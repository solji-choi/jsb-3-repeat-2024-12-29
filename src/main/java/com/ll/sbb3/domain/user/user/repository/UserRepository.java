package com.ll.sbb3.domain.user.user.repository;

import com.ll.sbb3.domain.user.user.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    SiteUser findByUsername(String username);
}
