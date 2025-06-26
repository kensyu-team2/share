package app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entity.Lending;
import app.entity.Reservation;
import app.repository.ReservationRepository;
import app.service.LendingService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/work")
@RequiredArgsConstructor
public class WorkController {
	private final ReservationRepository reservationRepository;
	//業務連絡メニュー画面を表示
	@RequestMapping("/menu")
	public String showMenu() {
		return "work/work_menu";
	}
	//	//メインメニューに戻る
	//	@RequestMapping("/main")
	//	public String backHome() {
	//		return "main_menu";
	//	}

	//遅延一覧を表示
//	@RequestMapping("/delay/list")
//	public String showDelayList() {
//		return "work/delay_list";
//	}

	//予約一覧を表示
	@RequestMapping("/reserve/list")
	public String showReserveList(@RequestParam(value = "filter", required = false, defaultValue = "all") String filter,
	                              Model model) {
	    List<Reservation> reservations;
	    if ("reserved".equals(filter)) {
	        // 予約確保中のみ（StatusId: 3）を持つ資料に紐づく予約を取得
	        reservations = reservationRepository.findReservedOnly();
	    } else {
	        // 全件
	        reservations = reservationRepository.findAll();
	    }
	    model.addAttribute("reservations", reservations);
	    model.addAttribute("filter", filter);
	    return "work/reserve_list";
	}



	// 【予約キャンセル確認画面】表示
	@GetMapping("/reserve/cancel/confirm/{reserveId}")
	public String showCancelConfirm(@PathVariable("reserveId") Integer reserveId, Model model) {
		Reservation reservation = reservationRepository.findById(reserveId).orElse(null);
		if (reservation == null) {
			return "redirect:/work/reserve/list";
		}
		model.addAttribute("reservation", reservation);
		return "work/reserve_cancel_confirm";
	}

	// 【予約キャンセル処理】実行 → 完了画面へ
	@GetMapping("/reserve/cancel/complete/{reserveId}")
	public String cancelReservation(@PathVariable("reserveId") Integer reserveId, Model model) {
		Reservation reservation = reservationRepository.findById(reserveId).orElse(null);
		if (reservation == null) {
			return "redirect:/work/reserve/list";
		}
		// 予約削除処理
		reservationRepository.delete(reservation);
		model.addAttribute("reservation", reservation);
		return "work/reserve_cancel_complete";
	}

	private final LendingService lendingService;

    @RequestMapping("/delay/list")
    public String showDelayList(Model model) {
        List<Lending> overdueList = lendingService.getOverdueLendings();
        model.addAttribute("lendings", overdueList);
        model.addAttribute("today", LocalDate.now()); // 延滞日数のため
        return "work/delay_list";
    }
}

//	//業務連絡メニューに戻る
//	@RequestMapping("")