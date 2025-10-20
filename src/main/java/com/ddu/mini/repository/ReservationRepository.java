package com.ddu.mini.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	public List<Reservation> findByMember(Member member);
}
