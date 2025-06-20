package app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.entity.Category;
import app.entity.Type;
import app.form.BookForm;
import app.repository.CategoryRepository;
import app.repository.TypeRepository;
import app.service.BookService;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TypeRepository typeRepository;
    /**
     * 資料管理メニューを表示
     * (GET /book/menu)
     */
    @GetMapping("/menu")
    public String showMenu() {
        return "book/book_mgmt_menu";
    }
    /**
     * プルダウン用のマスタデータをModelに追加する共通メソッド
     */
    private void addMasterDataToModel(Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<Type> types = typeRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
    }

    /**
     * 資料登録フォーム画面を表示
     * (GET /book/register)
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // RedirectAttributesで渡されたエラーのあるフォームデータがなければ、
        // テスト用の初期データを入れたフォームを生成する
        if (!model.containsAttribute("bookForm")) {
            BookForm bookForm = new BookForm(); // まずはオブジェクトを作成

         // --- ここからテスト用の初期データをセット（修正版） ---
            bookForm.setIsbn("978-4-87311-565-8");
            bookForm.setBookName("リーダブルコード");
            bookForm.setBookRuby("リーダブルコード");
            bookForm.setAuthorName("Dustin Boswell");
            bookForm.setAuthorRuby("ダスティン ボズウェル");
            bookForm.setArrivalDate(LocalDate.of(2025, 6, 22));
            bookForm.setPublisher("test社");
            bookForm.setPublishDate(LocalDate.of(2012, 6, 22));
            bookForm.setCategoryId(3); // 'コンピュータ・IT'
            bookForm.setTypeId(1);     // '一般書（単行本）'
            // --- ここまで ---

            model.addAttribute("bookForm", bookForm); // データをセットしたオブジェクトをModelに追加
        }

        addMasterDataToModel(model);
        return "book/book_registration_input";
    }

    /**
     * 入力内容を検証し、問題なければ確認画面へリダイレクト
     * (POST /book/register)
     */
    @PostMapping("/register")
    public String confirm(@Validated @ModelAttribute BookForm bookForm,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("bookForm", bookForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bookForm", bindingResult);
            return "redirect:/book/register";
        }

        redirectAttributes.addFlashAttribute("bookForm", bookForm);
        return "redirect:/book/confirm";
    }

    /**
     * 確認画面を表示
     * (GET /book/confirm)
     */
    @GetMapping("/confirm")
    public String showConfirm(Model model) {
        if (!model.containsAttribute("bookForm")) {
            return "redirect:/book/register";
        }
        return "book/book_registation_confirm";
    }

    /**
     * DBへの登録処理を実行
     * (POST /book/execute)
     */
    @PostMapping("/execute")
    public String execute(@ModelAttribute BookForm bookForm, Model model) {
        try {
            bookService.registerBook(bookForm);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            addMasterDataToModel(model);
            return "book/book_registration_input";
        }

        return "redirect:/book/complete";
    }

    /**
     * 登録完了画面を表示
     * (GET /book/complete)
     */
    @GetMapping("/complete")
    public String showComplete() {
        return "book/book_registation_complete";
    }
}