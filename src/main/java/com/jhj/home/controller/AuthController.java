package com.jhj.home.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhj.home.dto.SiteUserDto;
import com.jhj.home.entity.SiteUser;
import com.jhj.home.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
//	// 회원 가입
//	@PostMapping(value = "/signup")
//	public ResponseEntity<?> signup(@RequestBody SiteUser req) {
//		// 아이디 중복 확인
//		if(userRepository.findByUsername(req.getUsername()).isPresent()) { // 해당 아이디가 존재하므로 가입 불가
//			return ResponseEntity.badRequest().body("이미 존재하는 아이디입니다."); // 가입 실패 -> 에러 메세지
//		} else {
//			req.setPassword(passwordEncoder.encode(req.getPassword())); // 비밀번호 암호화
//			userRepository.save(req);
//			return ResponseEntity.ok("회원 가입에 성공하셨습니다."); // 가입 성공 ->  성공 메세지
//			//return ResponseEntity.ok(req); // 가입 성공 -> 해당 엔티티 반환
//		}
//	}
	
	// 회원 가입(Validation 적용)
	@PostMapping(value = "/signup")
	public ResponseEntity<?> signup(@Valid @RequestBody SiteUserDto siteUserDto, BindingResult bindingResult) {
		
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
		
		SiteUser siteUser = new SiteUser(); // Entity
		siteUser.setUsername(siteUserDto.getUsername());
		siteUser.setPassword(siteUserDto.getPassword());
		
		// 아이디 중복 확인
		if(userRepository.findByUsername(siteUser.getUsername()).isPresent()) { // 해당 아이디가 존재하므로 가입 불가
			Map<String, String> error = new HashMap<>();
			error.put("iderror", "이미 존재하는 아이디입니다.");
			return ResponseEntity.badRequest().body(error); // 가입 실패 -> 에러 메세지
		} else {
			siteUser.setPassword(passwordEncoder.encode(siteUser.getPassword())); // 비밀번호 암호화
			userRepository.save(siteUser);
			return ResponseEntity.ok("회원 가입에 성공하셨습니다."); // 가입 성공 ->  성공 메세지
			//return ResponseEntity.ok(req); // 가입 성공 -> 해당 엔티티 반환
		}
	}

	
	// 로그인
	@GetMapping("/me") // 현재 로그인한 사용자 정보를 가져오는 요청 (나 자신의 정보 -> 현재 로그인한 유저의 정보)
	public ResponseEntity<?> me(Authentication auth) {
		return ResponseEntity.ok(Map.of("username", auth.getName()));
	}
	
}
