package com.ddu.mini.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddu.mini.dto.PostDto;
import com.ddu.mini.entity.Post;
import com.ddu.mini.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final AuthenticationManager authManager;

    private final SecurityFilterChain filterChain;

    private final PostService postService;

    PostController(PostService postService, SecurityFilterChain filterChain, AuthenticationManager authManager) {
        this.postService = postService;
        this.filterChain = filterChain;
        this.authManager = authManager;
    }
    @GetMapping
    public List<Post> postList () {
    	return postService.list();
    }
	
	@PostMapping
	public ResponseEntity<?> write (@Valid @RequestBody PostDto postDto, BindingResult bindingResult, Authentication auth) {
		if (auth == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 후 글을 작성해주세요");
		}
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
			String email = auth.getName();
			Post savePost = postService.writePost(postDto, email);
			return ResponseEntity.ok(savePost);
		} catch (Exception e) {
			Map<String, String> errors = new HashMap<>();
            errors.put("postError", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> view (@PathVariable("id") Long id) {
		Optional<Post> _post = postService.viewPost(id);
		if (_post.isPresent()) {
			return ResponseEntity.ok(_post.get());
		} else {
			return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다");
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> update (@PathVariable("id") Long id, @Valid @RequestBody PostDto postDto, Authentication auth) {
		Optional<Post> _post = postService.findbyId(id);
		if (_post.isEmpty()) {
			return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다.");
		} 
		if (auth == null || !auth.getName().equals(_post.get().getAuthor().getEmail()) ) {
			return ResponseEntity.status(403).body("해당 게시글에 대한 권한이 없습니다.");
		}
		
		Post updatePost = postService.updatePost(postDto, id);
		return ResponseEntity.ok(updatePost);
		
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") Long id, Authentication auth) {
		Optional<Post> _post = postService.findbyId(id);
		if (_post.isEmpty()) {
			return ResponseEntity.status(404).body("해당 게시글이 존재하지 않습니다.");
		} 
		if (auth == null || !auth.getName().equals(_post.get().getAuthor().getEmail()) ) {
			return ResponseEntity.status(403).body("해당 게시글에 대한 권한이 없습니다.");
		}
		postService.deletePost(id);
		return ResponseEntity.ok("글 삭제 성공");
	}
	
}
