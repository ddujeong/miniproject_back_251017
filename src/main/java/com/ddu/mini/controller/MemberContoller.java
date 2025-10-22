package com.ddu.mini.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddu.mini.dto.MemberDto;
import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Post;
import com.ddu.mini.entity.Reservation;
import com.ddu.mini.service.MemberService;
import com.ddu.mini.service.PostService;
import com.ddu.mini.service.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/member")
public class MemberContoller {

	@Autowired
	MemberService memberService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	ReservationService reservationService;
	
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
	@GetMapping("/profile")
	public ResponseEntity<?> myPage(Authentication authentication) {
		if (authentication == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body("로그인이 필요합니다.");
	    }
		Member member= memberService.findMember(authentication.getName()).orElseThrow();
		List<Post> myPosts = postService.myPost(member);
		List<Reservation> myReservations = reservationService.myList(member);
		
		Map<String, Object> result = new HashMap<>();
		result.put("member", member);
		result.put("posts", myPosts);
		result.put("myReservations", myReservations);
		
		return ResponseEntity.ok(result);
	}
	@PutMapping("/profile")
	public ResponseEntity<?> updateMember(Authentication auth, @Valid@RequestBody MemberDto memberDto, BindingResult bindingResult) {
		if (auth == null) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body("로그인이 필요합니다.");
	    }
		if (memberDto.getPassword() ==null || memberDto.getPassword().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호는 필수 입력 사항입니다.");
		}
		
		String email = auth.getName();
        Member member = memberService.updateMember(email, memberDto, encoder);
        return ResponseEntity.ok(member);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMember(@PathVariable("id") Long id, Authentication auth){
		Optional<Member> _member = memberService.findById(id);
		if (_member.isEmpty()) {
			return ResponseEntity.status(404).body("존재하지 않는 사용자입니다.");
		}
		if (auth == null || !auth.getName().equals(_member.get().getEmail()) ) {
			return ResponseEntity.status(403).body("해당 권한이 없습니다.");
		}
		memberService.deleteMember(id);
		return ResponseEntity.ok("회원 탈퇴 성공");
	}

	
}
