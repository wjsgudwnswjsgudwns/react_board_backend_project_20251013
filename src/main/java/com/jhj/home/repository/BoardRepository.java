package com.jhj.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.home.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
