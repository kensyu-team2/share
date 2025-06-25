package app.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

import app.entity.Item;
import app.entity.Member;
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

        System.out.println("--- ★デバッグ★ showInputForm メソッド開始 ---"); // ← 追加

        if (!model.containsAttribute("lendingForm")) {

            System.out.println("--- ★デバッグ★ modelにlendingFormを追加します ---"); // ← 追加
            model.addAttribute("lendingForm", new LendingForm());

        } else {

            System.out.println("--- ★デバッグ★ modelにlendingFormは既に存在します ---"); // ← 追加
        }

        System.out.println("--- ★デバッグ★ lending/lending_input を返します ---"); // ← 追加

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

        // --- ★★★ ここから修正・追加 ★★★ ---

        // 1. itemIdsリストから、空や空白の要素を取り除く
        if (lendingForm.getItemIds() != null) {
            List<String> nonEmptyItemIds = lendingForm.getItemIds().stream()
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.toList());
            lendingForm.setItemIds(nonEmptyItemIds);
        }

        // 2. 手動でバリデーションを実行
        // ※LendingServiceに@Autowired private Validator validator; を追加し、コンストラクタで初期化してください。
        // validator.validate(lendingForm, bindingResult);
        // ↑↑↑ validatorの準備が複雑なため、より簡単な方法として、Service層でチェックする方針も良いです。
        // ここでは、一旦手動バリデーションの行はコメントアウトし、基本的なバリデーションに進みます。

        // --- ★★★ ここまで修正・追加 ★★★ ---


        // --- DTOのアノテーションによる基本的な入力値検証 ---
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.lendingForm", bindingResult);
            return "redirect:/lending/input";
        }

        // (もしリストが空になった場合の追加チェック)
        if (lendingForm.getItemIds().isEmpty()) {
            redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
            // ここでグローバルなエラーメッセージを追加できます
            redirectAttributes.addFlashAttribute("errorMessage", "貸出希望の資料を1冊以上入力してください。");
            return "redirect:/lending/input";
        }

        // 検証OKなら、入力内容を次のリクエストに渡して確認画面へリダイレクト
        redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
        return "redirect:/lending/confirm";
    }

    /**
     * 確認画面を表示
     * (GET /lending/confirm)
     */
    @GetMapping("/confirm")
 // ★修正点1: 引数に @ModelAttribute("lendingForm") LendingForm form を追加
 public String showConfirm(@ModelAttribute("lendingForm") LendingForm form, Model model) {

     // ★修正点2: model.getAttribute() の行は不要になるので削除
     // このメソッドが呼ばれる時点で、引数'form'にはRedirectAttributesで渡された値がセットされている。

     // Serviceを呼び出して、確認画面に表示するためのエンティティを取得
     Member member = lendingService.findMemberById(Integer.parseInt(form.getMemberId()));
     List<Item> items = lendingService.findItemsByIds(form.getItemIds());

     // 日付情報を生成
     LocalDate lendDate = LocalDate.now();
     LocalDate dueDate = lendDate.plusWeeks(2);

     // 取得したエンティティや日付を、個別にModelに追加する
     model.addAttribute("lendingForm", form); //★hiddenフィールドで使うために、元のformもModelに追加
     model.addAttribute("member", member);
     model.addAttribute("items", items);
     model.addAttribute("lendDate", lendDate);
     model.addAttribute("dueDate", dueDate);

     return "lending/lending_confirm";
 }

    /**
     * DBへの登録処理（貸出処理）を実行
     * (POST /lending/execute)
     */
 // LendingController.java

    @PostMapping("/execute")
    public String execute(@ModelAttribute LendingForm lendingForm, RedirectAttributes redirectAttributes) {
        try {
            // ★ Serviceの貸出実行メソッドを呼び出す
            lendingService.executeLending(lendingForm);
        } catch (RuntimeException e) {
            // 実行時エラーが発生した場合のハンドリング
            redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
            redirectAttributes.addFlashAttribute("errorMessage", "貸出処理中にエラーが発生しました：" + e.getMessage());
            return "redirect:/lending/input";
        }

        // 正常に完了したら完了メッセージを添えて完了画面へリダイレクト
        redirectAttributes.addFlashAttribute("message", "貸出処理が正常に完了しました。");
        System.out.println("redirect:/lending/complete");
        return "redirect:/lending/complete";

    }

    /**
     * 登録完了画面を表示
     * (GET /lending/complete)
     */
    @GetMapping("/complete")
    public String showComplete() {
    	System.out.println("lending/lending_complete");
        return "lending/lending_complete";
    }
}