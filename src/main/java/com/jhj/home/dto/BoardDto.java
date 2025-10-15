package com.jhj.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {

	@NotBlank(message = "제목을 입력해주세요")
	@Size(min = 5, message = "글 제목은 최소 5글자입니다.")
	private String title;
	
	@NotBlank(message = "내용을 입력해주세요")
	@Size(min = 5, message = "글 내용은 최소 5글자입니다.")
	private String content;
}
