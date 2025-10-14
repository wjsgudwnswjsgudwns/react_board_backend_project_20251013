package com.jhj.home.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.home.entity.Board;
import com.jhj.home.repository.BoardRepository;
import com.jhj.home.repository.UserRepository;

@RestController
@RequestMapping("/api/board")
public class BoardController {
		
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	// 전체 게시글 조회
	@GetMapping
	public List<Board> list() {
		return boardRepository.findAll();
	}
}
