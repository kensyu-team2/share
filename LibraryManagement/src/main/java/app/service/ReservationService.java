package app.service;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Service;

import app.entity.Reservation;
import app.repository.ReservationRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    @EntityGraph(attributePaths = {"books"})
    public List<Reservation> findByMemberId(Integer memberId) {
        return reservationRepository.findByMember_MemberId(memberId);
    }
	public List<Reservation> getReservationsByMemberId(Integer memberId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
}

