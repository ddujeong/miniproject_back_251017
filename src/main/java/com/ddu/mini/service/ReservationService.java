package com.ddu.mini.service;

import java.util.List;
import java.util.Optional;
import com.ddu.mini.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ddu.mini.dto.ReservationDto;
import com.ddu.mini.entity.Member;
import com.ddu.mini.entity.Reservation;
import com.ddu.mini.repository.MemberRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

	@Autowired
	MemberRepository memberRepository;

    ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
	
	public Optional<Reservation> findbyId (Long id) {
		return reservationRepository.findById(id);
	}
	public Reservation newReservation (ReservationDto reservationDto, String email) {
		Member member = memberRepository.findByEmail(email).orElseThrow();
		
		Reservation reservation = new Reservation();
		
		reservation.setCategory(reservationDto.getCategory());
		reservation.setService(reservationDto.getService());
		reservation.setReservationdatetime(reservationDto.getReservationdatetime());
		reservation.setMember(member);
		reservation.setStatus("예약");
		
		return reservationRepository.save(reservation);
	}
	public List<Reservation> myList (Member member){
		return reservationRepository.findByMember(member);
		
	}
	public void deleteReservation (Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow();
		
		reservationRepository.delete(reservation);
	}
	public Reservation updateReservation (ReservationDto reservationDto, Long id) {
		Reservation oldReservation = reservationRepository.findById(id).orElseThrow();
		
		oldReservation.setCategory(reservationDto.getCategory());
		oldReservation.setService(reservationDto.getService());
		oldReservation.setReservationdatetime(reservationDto.getReservationdatetime());
		
		return reservationRepository.save(oldReservation);
	}
	public List<Reservation> allList() {
		return reservationRepository.findAll();
	}
}
