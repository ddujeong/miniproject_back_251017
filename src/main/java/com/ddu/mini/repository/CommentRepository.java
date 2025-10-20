package com.ddu.mini.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.mini.entity.Comment;
import com.ddu.mini.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	public List<Comment> findByPost(Post post);
}
