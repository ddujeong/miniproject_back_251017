package com.ddu.mini.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
	
	@NotBlank(message = "제목을 입력하세요.")
	@Size(min = 5, message = "제목은 5글자 이상이어야 합니다.")
	private String title;
	
	@NotBlank(message = "내용을 입력하세요.")
	@Size(min = 10, message = "내용은 10글자 이상이어야 합니다.")
	private String content;
	
}
