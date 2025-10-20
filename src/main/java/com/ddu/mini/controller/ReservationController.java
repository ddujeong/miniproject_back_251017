package com.ddu.mini.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddu.mini.dto.ReservationDto;
import com.ddu.mini.entity.Reservation;
import com.ddu.mini.service.ReservationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
	
	@PostMapping
	public ResponseEntity<?> reserve (@Valid @RequestBody ReservationDto reservationDto,BindingResult bindingResult, Authentication auth ) {
		if (auth == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 후 예약 가능합니다.");
		}
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(
					err -> {
						errors.put(err.getField(), err.getDefaultMessage());
					}
					);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
		try {
			String email = auth.getName();
			Reservation reserve = reservationService.newReservation(reservationDto, email);
			return ResponseEntity.ok(reserve);
		} catch (Exception e) {
			Map<String, String> errors = new HashMap<>();
            errors.put("reserveError", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}
	}
}
