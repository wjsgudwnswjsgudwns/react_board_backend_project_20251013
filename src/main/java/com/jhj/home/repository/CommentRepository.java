package com.jhj.home.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.home.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
