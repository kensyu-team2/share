package jp.co.systempack.itemManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.systempack.itemManagement.entity.Item;
import jp.co.systempack.itemManagement.form.ItemForm;
import jp.co.systempack.itemManagement.service.ItemService;

@Controller
@RequestMapping("item")
public class ItemDeleteController {

	/** itemサービス. */
	@Autowired
	private ItemService itemService;

	/**
	 * 削除確認画面.
	 *
	 * @param itemForm  商品
	 * @param model モデル
	 * @return View
	 */
	@RequestMapping(value = "delete/confirm", method = { RequestMethod.GET, RequestMethod.POST })
	public String confirm(@ModelAttribute("item") ItemForm itemForm, Model model) {

		Item item = itemService.getItem(itemForm.getId());
		itemForm.setName(item.getName());
		itemForm.setPrice(item.getPrice());

		return "item/delete_confirm";
	}

	@RequestMapping(value = "delete/cancel", method = { RequestMethod.POST })
	public String confirmCancel() {
		return "redirect:../list";
	}

	/**
	 * 削除処理.
	 *
	 * @param itemForm          商品
	 * @param model         モデル
	 * @param redirectAttrs リダイレクト属性
	 * @return View
	 */
	@RequestMapping(value = "delete/delete", method = { RequestMethod.POST })
	public String delete(@ModelAttribute("item") ItemForm itemForm, Model model, RedirectAttributes redirectAttrs) {

		// 削除処理
		itemService.delete(itemForm.getId());
		redirectAttrs.addFlashAttribute("id", itemForm.getId());

		return "redirect:complete";
	}

	/**
	 * 削除完了画面.
	 *
	 * @return View
	 */
	@RequestMapping(value = "delete/complete", method = { RequestMethod.GET })
	public String complete() {
		return "item/delete_complete";
	}
}
