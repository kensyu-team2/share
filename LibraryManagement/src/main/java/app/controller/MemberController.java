package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import app.entity.Member;
import app.service.MemberService;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

 // 会員一覧
    @GetMapping("/member_list")
    public String showMemberList() {
        return "member/member_list"; // このファイルを返す
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
        member.setId(id);
        memberService.save(member);
        return "redirect:/member";
    }

    // 会員削除処理
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member";
    }
}
