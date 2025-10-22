package com.ddu.mini.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	
	public List<Post>findByAuthor(Member author);
	
	public Post findByHitIsGreaterThanEqual (Long hit);
	
	public Page<Post> findByCategory(String category, Pageable pageable);
}
