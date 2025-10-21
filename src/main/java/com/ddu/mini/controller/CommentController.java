package com.ddu.mini.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddu.mini.dto.CommentDto;
import com.ddu.mini.entity.Comment;
import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Post;
import com.ddu.mini.service.CommentService;
import com.ddu.mini.service.MemberService;
import com.ddu.mini.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
	
	@Autowired
	CommentService commentService;
	
	@Autowired
	PostService postService;
	
	@Autowired
	MemberService memberService;

	@PostMapping("/{boardId}")
	public ResponseEntity<?> write(@PathVariable("boardId") Long boardId,@Valid @RequestBody CommentDto commentDto, Authentication auth, BindingResult bindingResult ){
		Optional<Post> _post = postService.findbyId(boardId);
		if (_post.isEmpty()) {
			return ResponseEntity.status(404).body("존재하지 않는 게시글 입니다.");
		}
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
		
		String email = auth.getName();
		
		memberService.findMember(email).orElseThrow();
		Comment comment= commentService.writeComment(boardId, email, commentDto);
		return ResponseEntity.ok(comment);
	}
	@GetMapping("/{boardId}")
	public ResponseEntity<?> list(@PathVariable("boardId") Long boardId){
		Optional<Post> _post = postService.findbyId(boardId);
		
		if (_post.isEmpty()) {
			return ResponseEntity.status(404).body("존재하지 않는 게시글 입니다.");
		}
		List<Comment> comments = commentService.commentList(_post.get());
		return ResponseEntity.ok(comments);
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable("id") Long id,@Valid@RequestBody CommentDto commentDto, Authentication auth, BindingResult bindingResult ){
		Optional<Comment> _comment = commentService.findById(id);
		if(_comment.isEmpty()) {
			return ResponseEntity.status(404).body("존재하지 않는 댓글 입니다.");
		}
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
		
		String email = auth.getName();
		if(!_comment.get().getAuthor().getEmail().equals(email)) {
			return ResponseEntity.status(403).body("해당 댓글에 대한 수정 권한이 없습니다.");
		}
		Comment comment = commentService.updateComment(id, commentDto);
		return ResponseEntity.ok(comment);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id")Long id, Authentication auth){
		Optional<Comment> _comment = commentService.findById(id);
		if(_comment.isEmpty()) {
			return ResponseEntity.status(404).body("존재하지 않는 댓글 입니다.");
		}
		if (auth == null) {
			return ResponseEntity.status(403).body("해당 댓글에 대한 권한이 없습니다.");
		}
		String email = auth.getName();
		if(!_comment.get().getAuthor().getEmail().equals(email)) {
			return ResponseEntity.status(403).body("해당 댓글에 대한 수정 권한이 없습니다.");
		}
		commentService.deleteComment(id);
		return ResponseEntity.ok("댓글 삭제 성공");
	}
}
