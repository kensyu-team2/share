//package controller;
//
//import java.util.List;
//
//import javax.validation.Valid;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.sun.java.util.jar.pack.Package.Class.Member;
//
//import form.MemberForm;
//import form.MemberSearchForm;
//import service.MemberService;
//
//@Controller
//@RequestMapping("/members")
//public class MemberController {
//
//    private final MemberService service;
//
//    public MemberController(MemberService service) {
//        this.service = service;
//    }
//
//    /** 会員一覧 */
//    @GetMapping
//    public String list(Model m) {
//        List<Member> list = service.findAll();
//        m.addAttribute("members", list);
//        return "member/list";
//    }
//
//    /** 新規登録フォーム */
//    @GetMapping("/new")
//    public String createForm(Model m) {
//        m.addAttribute("memberForm", new MemberForm());
//        return "member/form";
//    }
//
//    /** 登録処理 */
//    @PostMapping
//    public String create(@Valid @ModelAttribute MemberForm form, BindingResult br) {
//        if (br.hasErrors()) {
//            return "member/form";
//        }
//        service.register(form);
//        return "redirect:/members";
//    }
//
//    /** 編集フォーム */
//    @GetMapping("/{id}/edit")
//    public String editForm(@PathVariable Long id, Model m) {
//        MemberForm form = service.loadForm(id);
//        m.addAttribute("memberForm", form);
//        return "member/form";
//    }
//
//    /** 更新処理 */
//    @PostMapping("/{id}")
//    public String update(@PathVariable Long id,
//                         @Valid @ModelAttribute MemberForm form, BindingResult br) {
//        if (br.hasErrors()) {
//            return "member/form";
//        }
//        service.update(id, form);
//        return "redirect:/members";
//    }
//
//    /** 削除処理 */
//    @PostMapping("/{id}/delete")
//    public String delete(@PathVariable Long id) {
//        service.delete(id);
//        return "redirect:/members";
//    }
//
//    /** 会員検索（MemberSearchForm） */
//    @GetMapping("/search")
//    public String search(@ModelAttribute MemberSearchForm form, Model m) {
//        List<Member> results = service.search(form);
//        m.addAttribute("members", results);
//        return "member/list";  // 一覧テンプレートを再利用
//    }
//
//}