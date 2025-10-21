package com.ddu.mini.service;
import com.ddu.mini.repository.PostRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddu.mini.dto.PostDto;
import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Post;
import com.ddu.mini.repository.MemberRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
	
	@Autowired
	MemberRepository memberRepository;

    PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Optional<Post> findbyId (Long id) {
    	return postRepository.findById(id);
    }
	
	public Post writePost (PostDto postDto, String email) {
		Member author = memberRepository.findByEmail(email).orElseThrow();
		
		Post post = new Post();

		post.setCategory(postDto.getCategory());
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setAuthor(author);
		
		return postRepository.save(post);
	}
	
	public List<Post> list () {
		return postRepository.findAll();
	}
	@Transactional
	public Post viewPost (Long id) {
		
		Post post = postRepository.findById(id).orElseThrow();
		post.setHit(post.getHit() + 1);
		
		return post;
	}
	public Post updatePost (PostDto postDto,Long id) {
		Post oldpost = postRepository.findById(id).orElseThrow();
		
		oldpost.setCategory(postDto.getCategory());
		oldpost.setTitle(postDto.getTitle());
		oldpost.setContent(postDto.getContent());
		
		return postRepository.save(oldpost);
		
	}
	public void deletePost(Long id) {
		Post post = postRepository.findById(id).orElseThrow();
		
		postRepository.delete(post);
	}
	public List<Post> myPost (Member member) {
		return postRepository.findByAuthor(member);
	}
}
