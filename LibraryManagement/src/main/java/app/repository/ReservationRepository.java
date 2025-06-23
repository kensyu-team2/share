package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Reservation;

@Repository

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @EntityGraph(attributePaths = {"book"})
    List<Reservation> findByMember_MemberId(Integer memberId);
}

