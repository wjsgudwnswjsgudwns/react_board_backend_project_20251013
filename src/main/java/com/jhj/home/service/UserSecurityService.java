package com.jhj.home.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jhj.home.entity.SiteUser;
import com.jhj.home.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;

	// spring security -> 유저에게 받은 username, password 조회
	// 아이디가 존재하지 않으면 "사용자 없음" 에러 발생
	// 아이디가 존재하면 password 확인 -> 암호화가 된 password 확인
	// 성공하면 권한을 부여 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		SiteUser siteUser = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));
		
		return org.springframework.security.core.userdetails.User.withUsername(siteUser.getUsername()).password(siteUser.getPassword()).authorities("User").build();
	}
	
}
