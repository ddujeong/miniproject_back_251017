package com.ddu.mini.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ddu.mini.dto.MemberDto;
import com.ddu.mini.entity.Member;
import com.ddu.mini.repository.MemberRepository;


@Service
public class MemberService {
		
	@Autowired
	MemberRepository memberRepository;
	
	public Optional<Member> findMember (String email) {
		return memberRepository.findByEmail(email);
	}
	
	public Member joinMember (MemberDto memberDto, PasswordEncoder encoder) {
		if(findMember(memberDto.getEmail()).isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
		}
		Member member = new Member();
		member.setName(memberDto.getName());
		member.setEmail(memberDto.getEmail());
		member.setPassword(encoder.encode( memberDto.getPassword()));
		return memberRepository.save(member);
	}
	public Member updateMember (String email,MemberDto memberDto, PasswordEncoder encoder) {
		Member member = memberRepository.findByEmail(email).orElseThrow();
		member.setName(memberDto.getName());
		member.setPassword(encoder.encode(memberDto.getPassword()));
		
		return memberRepository.save(member);
	}
}
