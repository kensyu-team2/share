package jp.co.systempack.itemManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.systempack.itemManagement.entity.Item;
import jp.co.systempack.itemManagement.form.ItemForm;
import jp.co.systempack.itemManagement.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemInsertController {

	/** itemサービス. */
	@Autowired
	private ItemService itemService;

	/**
	 * 入力画面表示メソッド.
	 *
	 * @param itemForm itemフォーム
	 * @return
	 */
	@RequestMapping(value = "insert/input", method = { RequestMethod.GET, RequestMethod.POST })
	public String input(@ModelAttribute("item") ItemForm itemForm) {
		// 入力画面を表示するためだけのメソッド
		return "item/insert_input";
	}

	/**
	 * 確認画面表示メソッド.
	 *
	 * @param itemForm   商品
	 * @param result バインド結果
	 * @return View
	 */
	@RequestMapping(value = "insert/confirm", method = { RequestMethod.POST })
	public String confirm(@ModelAttribute("item") @Validated ItemForm itemForm, BindingResult result) {

		if (result.hasErrors()) {
			return "item/insert_input";
		}

		return "item/insert_confirm";
	}

	/**
	 * 登録入力画面.
	 *
	 * @param itemForm          商品
	 * @param result        バインド結果
	 * @param redirectAttrs リダイレクト属性
	 * @return redirect
	 */
	@RequestMapping(value = "insert/insert", method = { RequestMethod.POST })
	public String insert(@ModelAttribute("item") @Validated ItemForm itemForm, BindingResult result,
			RedirectAttributes redirectAttrs) {

		if (result.hasErrors()) {
			return "item/insert_input";
		}

		// フォームからエンティティへの変換
		Item item = new Item();
		item.setId(itemForm.getId());
		item.setName(itemForm.getName());
		item.setPrice(itemForm.getPrice());

		// 登録処理
		item = itemService.insert(item);
		itemForm.setId(item.getId());

		redirectAttrs.addFlashAttribute("item", itemForm);

		return "redirect:complete";
	}

	/**
	 * 登録完了画面.
	 *
	 * @return View
	 */
	@RequestMapping(value = "insert/complete", method = { RequestMethod.GET })
	public String complete() {
		// 画面を表示するだけなので処理はなし。
		return "item/insert_complete";
	}

}
