package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import entity.Item;
import form.ItemForm;
import service.LendingService;

@Controller
@RequestMapping("item")
public class MemberController {

	/** itemサービス. */
	@Autowired
	private LendingService itemService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 入力画面表示メソッド.
	 *
	 * @param itemForm itemフォーム
	 * @return View
	 */
	@RequestMapping(value = "update/input", method = { RequestMethod.GET, RequestMethod.POST })
	public String input(@ModelAttribute("item") ItemForm itemForm) {

		Item item = itemService.getItem(itemForm.getId());
		itemForm.setName(item.getName());
		itemForm.setPrice(item.getPrice());
		itemForm.setVersion(item.getVersion());

		return "item/update_input";
	}

	/**
	 * 確認画面表示メソッド.
	 *
	 * @param itemForm   商品
	 * @param result バインド結果
	 * @return View
	 */
	@RequestMapping(value = "update/confirm", method = { RequestMethod.POST })
	public String confirm(@ModelAttribute("item") @Validated ItemForm itemForm, BindingResult result) {

		if (result.hasErrors()) {
			return "item/update_input";
		}

		return "item/update_confirm";
	}

	/**
	 * 更新処理.
	 *
	 * @param itemForm 商品
	 * @param result バインド結果
	 * @return Redirect
	 * @throws Exception
	 */
	@RequestMapping(value = "update/update", method = { RequestMethod.POST })
	public String update(@ModelAttribute("item") @Validated ItemForm itemForm, BindingResult result,
			RedirectAttributes redirectAttrs) throws Exception {

		if (result.hasErrors()) {
			return "item/update_input";
		}

		// FormクラスからEntityへの変換
		Item item = new Item();
		item.setId(itemForm.getId());
		item.setName(itemForm.getName());
		item.setPrice(itemForm.getPrice());
		item.setVersion(itemForm.getVersion());

		// 更新処理
		itemService.update(item);

		redirectAttrs.addFlashAttribute("item", itemForm);
		return "redirect:complete";
	}

	/**
	 * 完了画面.
	 *
	 * @return View
	 */
	@RequestMapping(value = "update/complete", method = { RequestMethod.GET })
	public String complete() {
		// 画面を表示するだけなので処理はなし。
		return "item/update_complete";
	}

}
