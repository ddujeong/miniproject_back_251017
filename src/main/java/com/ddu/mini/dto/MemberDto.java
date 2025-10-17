package com.ddu.mini.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

	@NotBlank(message = "이름을 입력해주세요.")
	private String name;
	
	@NotBlank(message = "이메일을 입력해주세요.")
	@Email(message = "올바른 이메일 형식이 아닙니다.")
	private String email;
	
	@NotBlank(message = "비밀번호를 입력해주세요")
	@Size(min = 5, message = "비밀번호는 최소 5글자 이상입니다.")
	private String password;
}
