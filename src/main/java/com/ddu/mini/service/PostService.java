package com.ddu.mini.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddu.mini.repository.MemberRepository;

@Service
public class PostService {
	
	@Autowired
	MemberRepository authRepository;

}
