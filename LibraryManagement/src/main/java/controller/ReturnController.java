package controller;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import entity.Item;
import service.LendingService;

@Controller
@RequestMapping("item")
public class ReturnController {

	/** itemサービス. */
	@Autowired
	private LendingService itemService;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 一覧画面.
	 *
	 * @param model モデル.
	 * @return View
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET })
	public String list(Model model) {

		List<Item> itemList = itemService.selectAll();
		if (itemList.isEmpty()) {
			String message =messageSource.getMessage("list.empty.error", null, Locale.getDefault());
			model.addAttribute("message", message);
		}
		model.addAttribute("itemList", itemList);

		return "item/list";
	}
}
