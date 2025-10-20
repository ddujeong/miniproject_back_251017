package com.ddu.mini.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	public List<Post>findByAuthor(Member author);
}
