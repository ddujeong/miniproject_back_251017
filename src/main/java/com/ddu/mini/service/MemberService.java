package com.ddu.mini.service;

import java.awt.Point;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;

import com.ddu.mini.dto.MemberDto;
import com.ddu.mini.entity.Comment;
import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Post;
import com.ddu.mini.entity.Reservation;
import com.ddu.mini.repository.CommentRepository;
import com.ddu.mini.repository.MemberRepository;
import com.ddu.mini.repository.PostRepository;
import com.ddu.mini.repository.ReservationRepository;

import jakarta.transaction.Transactional;


@Service
public class MemberService {

    private final SecurityFilterChain filterChain;
		
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired
	CommentRepository commentRepository;
	
	@Autowired
	ReservationRepository reservationRepository;
	
	@Autowired
	PostRepository postRepository;

    MemberService(SecurityFilterChain filterChain) {
        this.filterChain = filterChain;
    }
    public Optional<Member> findById(Long id) {
    	return memberRepository.findById(id);
    }
	
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
		if (memberDto.getPassword() !=null && !memberDto.getPassword().isEmpty()) {
			member.setPassword(encoder.encode(memberDto.getPassword()));
		}
		member.setName(memberDto.getName());
		
		
		return memberRepository.save(member);
	}
	@Transactional
	public void deleteMember(Long id) {
		Member member = memberRepository.findById(id).orElseThrow();
		
		List<Comment> comments = commentRepository.findByAuthor(member);
		List<Reservation> reservations = reservationRepository.findByMember(member);
		List<Post> posts = postRepository.findByAuthor(member);
		
		
		postRepository.deleteAll(posts);
		commentRepository.deleteAll(comments);
		reservationRepository.deleteAll(reservations);
		memberRepository.delete(member);
	}
}
