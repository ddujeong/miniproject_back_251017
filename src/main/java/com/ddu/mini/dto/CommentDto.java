package com.ddu.mini.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
	
	@NotBlank(message = "댓글은 필수 입력 사항입니다.")
	@Size(min = 5, message = "댓글 내용은 최소 5글자 이상이어야 합니다.")
	private String content;
}
