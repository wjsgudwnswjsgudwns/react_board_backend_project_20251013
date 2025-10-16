package com.jhj.home.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jhj.home.entity.Board;
import com.jhj.home.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

	//댓글이 달린 원 게시글로 댓글 리스트 반환
	List<Comment> findByBoard(Board board);
}
