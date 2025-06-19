//package controller;
//
//import java.util.List;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import entity.Book;
//import form.BookSearchForm;
//import service.BookService;
//
//@Controller
//@RequestMapping("/books")
//public class BookController {
//
//    private final BookService service;
//    public BookController(BookService service) {
//        this.service = service;
//    }
//
//    // ── 既存CRUD略 ──
//
//    /** 書籍検索（BookSearchForm） */
//    @GetMapping("/search")
//    public String search(@ModelAttribute BookSearchForm form, Model m) {
//        List<Book> results = service.search(form);
//        m.addAttribute("books", results);
//        return "book/list";
//    }
//}