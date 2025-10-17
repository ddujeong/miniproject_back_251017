package com.ddu.mini.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.mini.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long>{

	public Optional<Member> findByEmail(String email);
}
