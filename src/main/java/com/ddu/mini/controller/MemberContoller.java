package com.ddu.mini.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddu.mini.dto.MemberDto;
import com.ddu.mini.entity.Member;
import com.ddu.mini.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/member")
public class MemberContoller {

	@Autowired
	MemberService memberService;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	AuthenticationManager authManager;
	
	@PostMapping("/signup")
	public ResponseEntity<?> join(@Valid @RequestBody MemberDto memberDto , BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
					err -> {
						errors.put(err.getField(), err.getDefaultMessage());
					}
					);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		try {
			memberService.joinMember(memberDto, encoder);
			return ResponseEntity.ok("회원가입 성공!");
		} catch (IllegalArgumentException e) {
			Map<String, String> errors = new HashMap<>();
            errors.put("emailError", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		
	}
	@GetMapping("/me")
	public ResponseEntity<?> me (Authentication authentication) {
		return ResponseEntity.ok(Map.of("email", authentication.getName()));
	}
}
