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

    @GetMapping("/work/reserve/list")
    public String showReserveList(@RequestParam(value = "filter", required = false, defaultValue = "all") String filter,
                                  Model model) {
        List<Reservation> reservationList;

        if ("hold".equals(filter)) {
            reservationList = reservationService.findHoldReservations(); // 独自実装
        } else {
            reservationList = reservationService.findAll();
        }

        model.addAttribute("reservationList", reservationList);
        return "work/reserve_list";
    }

}

