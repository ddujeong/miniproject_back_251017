package com.ddu.mini.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddu.mini.dto.CommentDto;
import com.ddu.mini.entity.Comment;
import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Post;
import com.ddu.mini.repository.CommentRepository;
import com.ddu.mini.repository.MemberRepository;
import com.ddu.mini.repository.PostRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

	@Autowired
	PostRepository postRepository;
	
	@Autowired
	MemberRepository memberRepository;

    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public Optional<Comment> findById (Long id) {
    	return commentRepository.findById(id);
    }
	
	public Comment writeComment (Long boardId, String email ,CommentDto commentDto) {
		Member author = memberRepository.findByEmail(email).orElseThrow();
		Post post = postRepository.findById(boardId).orElseThrow();
		
		Comment comment = new Comment();
		
		comment.setContent(commentDto.getContent());
		comment.setAuthor(author);
		comment.setPost(post);
		
		return commentRepository.save(comment);
	}
	public List<Comment> commentList (Post post) {
		
		return commentRepository.findByPost(post);
	}
	public Comment updateComment (Long id, CommentDto commentDto) {
		
		Comment oldComment = commentRepository.findById(id).orElseThrow();
		oldComment.setContent(commentDto.getContent());
		return commentRepository.save(oldComment);
	}
	public void deleteComment (Long id) {
		Comment comment = commentRepository.findById(id).orElseThrow();
		
		commentRepository.delete(comment);
	}
	
}
