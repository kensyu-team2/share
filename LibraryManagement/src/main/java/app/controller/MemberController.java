package app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "member/member_list";
    }



    // 会員登録画面表示
    @GetMapping("/member_registation")
    public String showRegistrationForm(Model model) {
        model.addAttribute("member", new Member());
        return "member/member_registation"; // 会員登録フォーム
    }

    // 会員登録処理
    @PostMapping("/member_registation")
    public String registerMember(@ModelAttribute Member member) {
        memberService.save(member);
        return "redirect:/member";
    }

    // 会員詳細表示・編集画面
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Member member = memberService.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + id));
        model.addAttribute("member", member);
        return "member/edit";
    }

    // 会員更新処理
    @PostMapping("/edit/{id}")
    public String updateMember(@PathVariable Long id, @ModelAttribute Member member) {
        member.setMemberId(id);
        memberService.save(member);
        return "redirect:/member";
    }

    // 会員削除処理
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member";
    }

 // 予約一覧表示
    @GetMapping("/member_reservation_list/{memberId}")
    public String showReservationList(@PathVariable Long memberId, Model model) {
        List<Reservation> reservations = reservationService.findByMemberId(memberId);
        Member member = memberService.findById(memberId).orElse(null);
        model.addAttribute("reservations", reservations);
        model.addAttribute("member", memberService.findById(memberId).orElse(null));
        return "member/member_reservation_list";  // 予約一覧表示用のテンプレートを作成
    }
}
