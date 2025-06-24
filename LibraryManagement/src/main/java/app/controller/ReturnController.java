package app.controller;

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

import app.entity.Lending;
import app.form.ReturnForm;
import app.service.ReturnService; // ★ReturnServiceをインポート

@Controller
@RequestMapping("/return")
public class ReturnController {

    @Autowired
    private ReturnService returnService; // ★注入するのはReturnService

    @GetMapping("/input")
    public String showReturnForm(Model model) {
        model.addAttribute("returnForm", new ReturnForm());
        return "return/return_input";
    }

    @PostMapping("/confirm")
    public String postToConfirm(@Validated @ModelAttribute ReturnForm returnForm,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        // 1. itemIdsリストから、空や空白の要素を取り除く
        if (returnForm.getItemIds() != null) {
            List<String> nonEmptyItemIds = returnForm.getItemIds().stream()
                .filter(id -> id != null && !id.trim().isEmpty())
                .collect(Collectors.toList());
            returnForm.setItemIds(nonEmptyItemIds);
        }

        // 2. DTOの@Size(min=1)によるチェック
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("returnForm", returnForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.returnForm", bindingResult);
            return "redirect:/return/input";
        }

        // 3. (参考)リストが空になった場合の追加チェック
        if (returnForm.getItemIds().isEmpty()) {
            redirectAttributes.addFlashAttribute("returnForm", returnForm);
            redirectAttributes.addFlashAttribute("errorMessage", "返却する資料を1冊以上入力してください。");
            return "redirect:/return/input";
        }

        // 検証OKなら、入力内容を次のリクエストに渡して確認画面へリダイレクト
        redirectAttributes.addFlashAttribute("returnForm", returnForm);
        return "redirect:/return/confirm";
    }

    /**
     * 返却確認画面を表示
     * (GET /return/confirm)
     */
    @GetMapping("/confirm")
    public String showConfirm(@ModelAttribute("returnForm") ReturnForm form, Model model, RedirectAttributes redirectAttributes) {

        try {
            // ★★★ここからが修正箇所です★★★
            // 1. Serviceを呼び出して、確認画面に表示する貸出記録のリストを取得
            List<Lending> lendingList = returnService.prepareReturnConfirmation(form);

            // もし返却対象が1件もなければ、エラーメッセージを添えて入力画面に戻す
            if (lendingList.isEmpty()) {
                redirectAttributes.addFlashAttribute("returnForm", form);
                redirectAttributes.addFlashAttribute("errorMessage", "返却対象の資料が見つかりませんでした。");
                return "redirect:/return/input";
            }

            // 2. 取得したリストを "lendingList" という名前でModelに追加
            model.addAttribute("lendingList", lendingList);

        } catch (RuntimeException e) {
            // Service内でエラー（例：存在しないID）が発生した場合の処理
            redirectAttributes.addFlashAttribute("returnForm", form);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/return/input";
        }

        // 次のexecute処理で使うため、元のフォームもModelに追加
        model.addAttribute("returnForm", form);

        return "return/return_confirm";
    }

    @PostMapping("/execute")
    public String executeReturn(@ModelAttribute ReturnForm returnForm, RedirectAttributes redirectAttributes) {
        try {

            List<Lending> processedLendings = returnService.executeReturn(returnForm);

            redirectAttributes.addFlashAttribute("processedLendings", processedLendings);

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("returnForm", returnForm);
            return "redirect:/return/input";
        }

        redirectAttributes.addFlashAttribute("message", "返却処理が完了しました。");
        return "redirect:/return/complete";
    }

    @GetMapping("/complete")
    public String showReturnComplete() {
        return "return/return_complete";
    }
}