package com.jhj.home.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.home.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

	public Optional<SiteUser> findByUsername(String username);
}
