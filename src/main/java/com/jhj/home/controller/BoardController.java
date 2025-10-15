package com.jhj.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.home.dto.BoardDto;
import com.jhj.home.entity.Board;
import com.jhj.home.entity.SiteUser;
import com.jhj.home.repository.BoardRepository;
import com.jhj.home.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

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
	
//	// 게시글 쓰기
//	@PostMapping
//	public ResponseEntity<?> write(@RequestBody Board req, Authentication auth) {
//		
//		SiteUser siteUser = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
//		// siteUser -> 현재로그인한 유저의 레코드 
//		
//		Board board = new Board();
//		board.setTitle(req.getTitle());
//		board.setContent(req.getContent());
//		board.setAuthor(siteUser);
//		boardRepository.save(board);
//		
//		return ResponseEntity.ok(board);
//	}
	
	// 게시글 쓰기 (Validation)
	@PostMapping
	public ResponseEntity<?> write(@Valid @RequestBody BoardDto boardDto, Authentication auth,BindingResult bindingResult) {
		
		//사용자의 로그인 여부 확인
		if (auth == null) { //참이면 로그인 x -> 글쓰기 권한 없음 -> 에러코드 반환  
			return ResponseEntity.status(401).body("로그인 후 글쓰기 가능합니다.");
		}
		
		// Spring Validation 결과 처리                              
		if(bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
					err -> {
						errors.put(err.getField(), err.getDefaultMessage());
					}
			);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
		SiteUser siteUser = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
		// siteUser -> 현재로그인한 유저의 레코드 
		
		Board board = new Board();
		board.setTitle(boardDto.getTitle());
		board.setContent(boardDto.getContent());
		board.setAuthor(siteUser);
		boardRepository.save(board);
		
		return ResponseEntity.ok(board);
	}
	
	// 특정 글 호출
	@GetMapping("{id}")
	public ResponseEntity<?> getPost(@PathVariable("id") Long id) {
//		Board board = boardRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시글"));
//		return board;
		Optional<Board> _board = boardRepository.findById(id);
		if(_board.isPresent()) {
			return ResponseEntity.ok(_board.get());
		} else {
			return ResponseEntity.status(404).body("존재하지 않는 게시글입니다.");
		}
		
	}
	
	// 특정 글 삭제
	@DeleteMapping("{id}")
	public ResponseEntity<?> deletePost(@PathVariable("id") Long id, Authentication auth) {
		
		Optional<Board> optionalBoard = boardRepository.findById(id);
	    if (optionalBoard.isEmpty()) {
	        return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
	    }

	    Board board = optionalBoard.get();

	    if (auth == null || !auth.getName().equals(board.getAuthor().getUsername())) {
	        return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
	    }

	    boardRepository.delete(board);
	    return ResponseEntity.ok("삭제 완료");
			
	}
	
	// 글 수정
	@PutMapping("{id}")
	public ResponseEntity<?> updatePost(@PathVariable("id") Long id, @RequestBody Board updateBoard, Authentication auth) {
		
		Optional<Board> optionalBoard = boardRepository.findById(id);
		if (optionalBoard.isEmpty()) {
	        return ResponseEntity.status(404).body("게시글이 존재하지 않습니다.");
	    }
		
		Board board = optionalBoard.get();
		
		if (auth == null || !auth.getName().equals(board.getAuthor().getUsername())) {
			return ResponseEntity.status(403).body("수정 권한이 없습니다.");
		}
		
		board.setTitle(updateBoard.getTitle());
		board.setContent(updateBoard.getContent());
		
		boardRepository.save(board);
		
		return ResponseEntity.ok(board);
		
	}
	
}
