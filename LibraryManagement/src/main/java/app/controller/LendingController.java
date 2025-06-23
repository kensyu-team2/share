package app.controller;

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

import app.form.LendingForm;
import app.service.LendingService; // 作成したLendingServiceをインポート

@Controller
@RequestMapping("/lending") // 貸出機能に関するURLは "/lending" で始まる
public class LendingController {

    @Autowired // LendingServiceをDIコンテナから注入
    private LendingService lendingService;

    /**
     * 貸出入力フォーム画面を表示
     * (GET /lending/input)
     */
    @GetMapping("/input")
    public String showInputForm(Model model) {
        // RedirectAttributesで渡されたエラーのあるフォームデータがなければ、新しいフォームを生成
        if (!model.containsAttribute("lendingForm")) {
            model.addAttribute("lendingForm", new LendingForm());
        }
        return "lending/lending_input";
    }

    /**
     * 入力内容を検証し、問題なければ確認画面へリダイレクト
     * (POST /lending/confirm)
     */
    @PostMapping("/confirm")
    public String postToConfirm(@Validated @ModelAttribute LendingForm lendingForm,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        // --- DTOのアノテーションによる基本的な入力値検証 ---
        if (bindingResult.hasErrors()) {
            // エラーがある場合は、入力内容とエラー結果を次のリクエストに渡して入力画面にリダイレクト
            redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.lendingForm", bindingResult);
            return "redirect:/lending/input";
        }

        // --- Service層でのビジネスルール検証 ---
        // try {
        //     // 例: 会員や資料の存在チェック、貸出上限チェックなどをServiceで実行
        //     lendingService.validateLendingRules(lendingForm);
        // } catch (RuntimeException e) { // 本来はカスタムの業務例外をキャッチ
        //     redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
        //     // グローバルなエラーメッセージとして追加
        //     redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        //     return "redirect:/lending/input";
        // }


        // 検証OKなら、入力内容を次のリクエストに渡して確認画面へリダイレクト
        redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
        return "redirect:/lending/confirm";
    }

    /**
     * 確認画面を表示
     * (GET /lending/confirm)
     */
    @GetMapping("/confirm")
    public String showConfirm(Model model) {
        // 確認画面に直接アクセスされた場合などを考慮し、フォームデータがなければ入力画面に戻す
        if (!model.containsAttribute("lendingForm")) {
            return "redirect:/lending/input";
        }

        // 必要であれば、確認画面に表示するための追加情報（会員名、書籍名など）を
        // Service経由で取得し、Modelに追加する
        // LendingForm form = (LendingForm) model.getAttribute("lendingForm");
        // model.addAttribute("confirmationData", lendingService.getConfirmationData(form));

        return "lending/lending_confirm";
    }

    /**
     * DBへの登録処理（貸出処理）を実行
     * (POST /lending/execute)
     */
    @PostMapping("/execute")
    public String execute(@ModelAttribute LendingForm lendingForm, RedirectAttributes redirectAttributes) {
        try {
            // Serviceの貸出実行メソッドを呼び出す
            lendingService.executeLending(lendingForm);
        } catch (RuntimeException e) {
            // 実行時（例：確認画面表示後、貸出実行までの間に在庫がなくなった場合など）のエラーハンドリング
            redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
            redirectAttributes.addFlashAttribute("errorMessage", "貸出処理中にエラーが発生しました：" + e.getMessage());
            return "redirect:/lending/input";
        }

        // 正常に完了したら完了画面へリダイレクト
        return "redirect:/lending/complete";
    }

    /**
     * 登録完了画面を表示
     * (GET /lending/complete)
     */
    @GetMapping("/complete")
    public String showComplete() {
        return "lending/lending_complete";
    }
}