package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entity.Reservation;
import app.repository.ReservationRepository;

@Controller
@RequestMapping("/work")
public class WorkController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping("/reserve/list")
    public String showReservationList(Model model) {
        List<Reservation> reservationList = reservationRepository.findAll();

        model.addAttribute("reservationList", reservationList);

        return "work/reserve_list";
    }
}
