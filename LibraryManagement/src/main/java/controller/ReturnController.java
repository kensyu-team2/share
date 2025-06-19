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
//import form.ReturnForm;
//import service.ReturnService;
//
//@Controller
//@RequestMapping("/returns")
//public class ReturnController {
//
//    private final ReturnService service;
//
//    public ReturnController(ReturnService service) {
//        this.service = service;
//    }
//
//    /** 返却フォーム表示 */
//    @GetMapping("/new")
//    public String newForm(Model m) {
//        m.addAttribute("returnForm", new ReturnForm());
//        m.addAttribute("lendings", service.findCurrentlyLent());
//        return "return/form";
//    }
//
//    /** 返却処理 */
//    @PostMapping
//    public String create(@Valid @ModelAttribute ReturnForm form, BindingResult br, Model m) {
//        if (br.hasErrors()) {
//            m.addAttribute("lendings", service.findCurrentlyLent());
//            return "return/form";
//        }
//        service.returnItem(form);
//        return "redirect:/returns/history";
//    }
//
//    /** 返却履歴 */
//    @GetMapping("/history")
//    public String history(Model m) {
//        m.addAttribute("records", service.findAllReturns());
//        return "return/history";
//    }
//}