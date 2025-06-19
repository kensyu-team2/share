//package controller;
//
//import javax.validation.Valid;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import form.LendingForm;
//import service.LendingService;
//
//@Controller
//@RequestMapping("/lendings")
//public class LendingController {
//
//    private final LendingService service;
//
//    public LendingController(LendingService service) {
//        this.service = service;
//    }
//
//    /** 貸出フォーム表示 */
//    @GetMapping("/new")
//    public String newForm(Model m) {
//        m.addAttribute("lendingForm", new LendingForm());
//        m.addAttribute("members", service.findAllMembers());
//        m.addAttribute("items", service.findAvailableItems());
//        return "lending/form";
//    }
//
//    /** 貸出処理 */
//    @PostMapping
//    public String create(@Valid @ModelAttribute LendingForm form, BindingResult br, Model m) {
//        if (br.hasErrors()) {
//            m.addAttribute("members", service.findAllMembers());
//            m.addAttribute("items", service.findAvailableItems());
//            return "lending/form";
//        }
//        service.lend(form);
//        return "redirect:/lendings/history";
//    }
//
//    /** 貸出履歴表示 */
//    @GetMapping("/history")
//    public String history(Model m) {
//        m.addAttribute("records", service.findAllLendings());
//        return "lending/history";
//    }
//}