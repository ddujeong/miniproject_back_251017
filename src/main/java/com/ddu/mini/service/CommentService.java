package com.ddu.mini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddu.mini.repository.MemberRepository;
import com.ddu.mini.repository.PostRepository;

@Service
public class CommentService {

	@Autowired
	PostRepository postRepository;
	
	@Autowired
	MemberRepository authRepository;
	
}
