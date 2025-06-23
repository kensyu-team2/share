package app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import app.entity.Reservation;
import app.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return reservationRepository.findByMember_MemberId(memberId);
    }
}

