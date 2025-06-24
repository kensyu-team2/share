package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entity.Reservation;
import app.repository.ReservationRepository;
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
	@RequestMapping("/delay/list")
	public String showDelayList() {
		return "work/delay_list";
	}

	//予約一覧を表示
	@RequestMapping("/reserve/list")
	public String showReserveList(Model model) {
	    model.addAttribute("reservations", reservationRepository.findAll());
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
}

//	//業務連絡メニューに戻る
//	@RequestMapping("")

