package app.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

		if (!model.containsAttribute("lendingForm")) {

			model.addAttribute("lendingForm", new LendingForm());

		} else {

		}

		return "lending/lending_input";
	}

	/**
	 * 入力内容を検証し、問題なければ確認画面へリダイレクト
	 * (POST /lending/confirm)
	 */
	@PostMapping("/confirm")
	public String postToConfirm(@ModelAttribute LendingForm lendingForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		// 1. まず、リストから空の要素を取り除いた、新しいリストを作成します
		List<String> nonEmptyItemIds;
		if (lendingForm.getItemIds() != null) {
			nonEmptyItemIds = lendingForm.getItemIds().stream()
					.filter(id -> id != null && !id.trim().isEmpty())
					.collect(Collectors.toList());
		} else {
			nonEmptyItemIds = new ArrayList<>();
		}

		// 2. 元のフォームオブジェクトのリストを、この新しいリストで上書きします
		lendingForm.setItemIds(nonEmptyItemIds);

		// 3. これ以降は、上書きされたlendingFormを使ってバリデーションを行います

		// 会員IDのチェック (手動バリデーション)
		String memberId = lendingForm.getMemberId();
		if (memberId == null || memberId.trim().isEmpty()) {
			bindingResult.rejectValue("memberId", "NotBlank.memberId", "会員IDを入力してください。");
		} else {
			if (!memberId.matches("^[0-9]+$")) {
				bindingResult.rejectValue("memberId", "Pattern.memberId", "会員IDは数字で入力してください。");
			}
			if (memberId.length() > 5) {
				bindingResult.rejectValue("memberId", "Size.memberId", "会員IDは５桁までで入力してください。");
			}
			// 会員IDの形式が正しい場合のみ、DBに存在するかをチェックします
			if (!bindingResult.hasFieldErrors("memberId")) {
				try {
					lendingService.findMemberById(Integer.parseInt(memberId));
				} catch (RuntimeException e) {
					// Serviceから例外が投げられたら、それは「会員が存在しない」ことを意味します
					// テスト仕様書通りのメッセージをセットします
					bindingResult.rejectValue("memberId", "NotExist.memberId", "存在しない会員IDが入力されています。");
				}
			}
		}

		// 資料IDのチェック (手動バリデーション)
		if (lendingForm.getItemIds().isEmpty()) {
			bindingResult.rejectValue("itemIds", "Size.itemIds", "資料IDを入力してください。");
		} else {
			for (String itemId : lendingForm.getItemIds()) {
				if (!itemId.matches("^[0-9]+$")) {
					bindingResult.rejectValue("itemIds", "Pattern.itemIds", "個別資料IDは数字で入力してください。");
				}
				if (itemId.length() > 10) {
					bindingResult.rejectValue("itemIds", "Size.itemIds", "個別資料IDは１０桁までで入力してください。");
				}
			}
		}

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.lendingForm",
					bindingResult);
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
			// Serviceの貸出実行メソッドを呼び出す
			lendingService.executeLending(lendingForm);

			Member member = lendingService.findMemberById(Integer.parseInt(lendingForm.getMemberId()));
			List<Item> items = lendingService.findItemsByIds(lendingForm.getItemIds());
			LocalDate lendDate = LocalDate.now();
			LocalDate dueDate = lendDate.plusWeeks(2);

			// 完了画面に渡すためのデータを"Flash Attribute"としてセットします。
			// HTMLの変数名と合わせます
			redirectAttributes.addFlashAttribute("completedMember", member);
			redirectAttributes.addFlashAttribute("completedItems", items);
			redirectAttributes.addFlashAttribute("completedLendDate", lendDate);
			redirectAttributes.addFlashAttribute("completedDueDate", dueDate);

		} catch (RuntimeException e) {
			// エラーハンドリング
			redirectAttributes.addFlashAttribute("lendingForm", lendingForm);
			redirectAttributes.addFlashAttribute("errorMessage", "貸出処理中にエラーが発生しました：" + e.getMessage());
			return "redirect:/lending/input";
		}

		// 完了メッセージを渡す
		redirectAttributes.addFlashAttribute("message", "貸出処理が正常に完了しました。");
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