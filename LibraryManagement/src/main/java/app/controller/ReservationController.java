package app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entity.Reservation;
import app.service.ReservationService;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/list")
    public String showReservations(@RequestParam("memberId") Integer memberId, Model model) {
        List<Reservation> reservations = reservationService.getReservationsByMemberId(memberId);
        model.addAttribute("reservations", reservations);
        return "reservation/list";  // Thymeleafなどのテンプレート名
    }
}

