package app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import app.entity.Member;
import app.entity.Reservation;
import app.service.MemberService;
import app.service.ReservationService;

@Controller
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;
	private final ReservationService reservationService;

	public MemberController(MemberService memberService, ReservationService reservationService) {
		this.memberService = memberService;
		this.reservationService = reservationService;
	}

	// 会員一覧
	@GetMapping("/member_list")
	public String showMemberList(Model model) {
	    List<Member> members = memberService.findAllActive();  // 退会者は除外して取得
	    model.addAttribute("members", members);
	    return "member/member_list";
	}


	// 会員登録入力
	@GetMapping("/member_registation")
	public String showRegistrationForm(Model model) {
		model.addAttribute("member", new Member());
		return "member/member_registation";
	}
	// 会員登録確認
	@PostMapping("/member_registation_confirm")
	public String confirmRegistration(@ModelAttribute("member") Member member, Model model) {
		model.addAttribute("member", member); // 明示的に渡す
		return "member/member_registation_confirm";
	}
	// 会員登録完了
	@PostMapping("/member_registation_complete")
	public String completeRegistration(@ModelAttribute("member") Member member) {
		memberService.save(member); // 登録日もここでセットされる
		return "member/member_registation_complete";
	}
	// 予約一覧表示
	@GetMapping("/member_reservation_list/{memberId}")
	public String showReservationList(@PathVariable Integer memberId, Model model) {
		List<Reservation> reservations = reservationService.findByMemberId(memberId);
		Member member = memberService.findById(memberId).orElse(null);
		model.addAttribute("reservations", reservations);
		model.addAttribute("member", memberService.findById(memberId).orElse(null));
		return "member/member_reservation_list";  // 予約一覧表示用のテンプレートを作成
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable("id") Integer id, Model model) {
		Member member = memberService.findById(id).orElse(null);
		if (member == null) {
			throw new IllegalArgumentException("Invalid member ID: " + id);
		}
		model.addAttribute("member", member);
		return "member/change_member_information";
	}

	@PostMapping("/edit/confirm")
	public String confirmEdit(@ModelAttribute("member") Member member, Model model) {
		model.addAttribute("member", member);
		return "member/change_member_information_confirm";
	}

	@PostMapping("/edit/complete")
	public String completeEdit(@ModelAttribute Member formMember, Model model) {
		// DBから元のMemberを取得（Optional対応）
		Optional<Member> optionalMember = memberService.findById(formMember.getMemberId());
		if (optionalMember.isPresent()) {
			Member existing = optionalMember.get();
			// 変更禁止項目を復元
			formMember.setBirthday(existing.getBirthday());
			formMember.setRegistrationDate(existing.getRegistrationDate());
			// 保存
			memberService.save(formMember);
			model.addAttribute("member", formMember);
			return "member/change_member_information_complete";

		} else {
			// ID不正などで会員が存在しない場合の処理
			model.addAttribute("errorMessage", "会員情報が見つかりませんでした。");
			return "error";
		}
	}

	// 退会確認画面表示
	@GetMapping("/withdrawal/confirm/{id}")
	public String confirmWithdrawal(@PathVariable("id") Integer memberId, Model model) {
	    Member member = memberService.findById(memberId).orElse(null);
	    if (member == null) {
	        model.addAttribute("errorMessage", "該当会員が見つかりません。");
	        return "error";
	    }
	    model.addAttribute("member", member);
	    return "member/member_withdrawal_confirm";
	}

	// 退会実行
	@PostMapping("/withdrawal/execute")
	public String executeWithdraw(@ModelAttribute Member member, Model model) {
	    Optional<Member> optionalMember = memberService.findById(member.getMemberId());
	    if (optionalMember.isPresent()) {
	        Member existing = optionalMember.get();
	        existing.setRetireDate(LocalDate.now());
	        memberService.save(existing);
	        model.addAttribute("member", existing);
	        return "member/member_withdrawal_complete";
	    } else {
	        model.addAttribute("errorMessage", "退会処理に失敗しました。");
	        return "error";
	    }
	}

//	会員検索
	@GetMapping("/search")
	public String searchMembers(
	    @RequestParam(name = "keyword", required = false) String keyword,
	    @RequestParam(name = "includeRetired", defaultValue = "false") boolean includeRetired,
	    Model model) {

	    List<Member> members = memberService.searchMembers(keyword, includeRetired);
	    model.addAttribute("members", members);
	    model.addAttribute("keyword", keyword);
	    model.addAttribute("includeRetired", includeRetired);
	    return "member/member_list";
	}



}

