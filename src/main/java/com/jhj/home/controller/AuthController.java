package com.jhj.home.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.home.entity.SiteUser;
import com.jhj.home.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping(value = "/signup")
	public ResponseEntity<?> signup(@RequestBody SiteUser req) {
		// 아이디 중복 확인
		if(userRepository.findByUsername(req.getUsername()).isPresent()) { // 해당 아이디가 존재하므로 가입 불가
			return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다."); // 가입 실패 -> 에러 메세지
		} else {
			req.setPassword(passwordEncoder.encode(req.getPassword())); // 비밀번호 암호화
			userRepository.save(req);
			return ResponseEntity.ok("회원 가입에 성공하셨습니다."); // 가입 성공 ->  성공 메세지
			//return ResponseEntity.ok(req); // 가입 성공 -> 해당 엔티티 반환
		}
	}

}
