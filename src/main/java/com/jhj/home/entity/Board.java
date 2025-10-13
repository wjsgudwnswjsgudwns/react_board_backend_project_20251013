package com.jhj.home.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 게시판 번호
	
	private String title; // 게시판 제목
	
	private String content; // 게시판 내용
	
	@CreationTimestamp // 자동으로 날짜 삽입
	private LocalDateTime createDate; // 작성일
	
	@ManyToOne
	private SiteUser author; // 글쓴이
}
