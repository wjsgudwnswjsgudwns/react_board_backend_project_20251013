package com.jhj.home.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.home.dto.CommentDto;
import com.jhj.home.entity.Board;
import com.jhj.home.entity.Comment;
import com.jhj.home.entity.SiteUser;
import com.jhj.home.repository.BoardRepository;
import com.jhj.home.repository.CommentRepository;
import com.jhj.home.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final BoardController boardController;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UserRepository userRepository;

    CommentController(BoardController boardController) {
        this.boardController = boardController;
    }
	
	@PostMapping("{boardId}")
	public ResponseEntity<?> writeComment(@PathVariable("boardId") Long boardId, @Valid @RequestBody CommentDto commentDto, BindingResult bindingResult, Authentication auth) {
		
		// 월 글 존재 여부 확인
		Optional<Board> _board = boardRepository.findById(boardId);
		if(_board.isEmpty()) {
			return ResponseEntity.badRequest().body("존재하지 않는 게시글입니다");
		}
		
		// 로그인 한 유저의 SiteUser 객체
		SiteUser user = userRepository.findByUsername(auth.getName()).orElseThrow();
		
		Comment comment = new Comment();
		comment.setBoard(_board.get());
		comment.setAuthor(user);
		comment.setContent(commentDto.getContent());
		
		commentRepository.save(comment);
		
		return ResponseEntity.ok(comment);
	}

}
