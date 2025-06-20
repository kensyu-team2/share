//package app.controller;
//
//import javax.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import org.springframework.web.bind.support.SessionStatus;
//
//import app.form.ReturnForm;
//import app.service.ReturnService;
//
///**
// * 返却
// *   ReturnConfirm, ReturnComplete
// */
//@Controller
//@RequestMapping("/returns")
//@SessionAttributes("returnForm")
//public class ReturnController {
//
//    private final ReturnService svc;
//
//    @Autowired
//    public ReturnController(ReturnService svc) {
//        this.svc = svc;
//    }
//
//    @GetMapping("/confirm")
//    public String showConfirm(@RequestParam Long loanId, Model m) {
//        m.addAttribute("returnForm", new ReturnForm(loanId));
//        return "return/confirm";
//    }
//
//    @PostMapping("/complete")
//    public String complete(
//            @Valid @ModelAttribute("returnForm") ReturnForm form,
//            BindingResult res,
//            SessionStatus st,
//            Model m) {
//        if (res.hasErrors()) return "return/confirm";
//        Return r = svc.returnBook(form.getLoanId(), form.getReturnDate());
//        st.setComplete();
//        m.addAttribute("returned", r);
//        return "return/complete";
//    }
//}