package app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.entity.Book; // 追加
import app.entity.Category;
import app.entity.Item;
import app.entity.Lending;
import app.entity.Member;
import app.entity.Reservation;
import app.entity.Type;
import app.form.BookForm;
import app.form.BookSearchForm; // 追加
import app.form.ItemActionForm;
import app.form.ReservationForm;
import app.repository.CategoryRepository;
import app.repository.MemberRepository;
import app.repository.ReservationRepository;
import app.repository.TypeRepository;
import app.service.BookService;
import app.service.ItemService; // ItemServiceを後で作成
import app.service.LendingService;

@Controller
@RequestMapping("book")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TypeRepository typeRepository;
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
    private ItemService itemService;


	@Autowired
    private LendingService lendingService;


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
			bookForm.setTypeId(1); // '一般書（単行本）'
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
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.bookForm",
					bindingResult);
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

	/**
	 * 資料検索フォームを表示
	 * (GET /book/search)
	 */
	@GetMapping("/search")
	public String showSearchForm(Model model) {
		model.addAttribute("bookSearchForm", new BookSearchForm());
		// プルダウン用のマスタデータをModelに追加
		addMasterDataToModel(model);
		return "book/book_search";
	}

	/**
	 * 検索を実行し、結果画面を表示
	 * (GET /book/search/results)
	 */
	@GetMapping("/search/results")
	public String searchBooks(@ModelAttribute BookSearchForm form,
			@PageableDefault(page = 0, size = 10) Pageable pageable, // sortのデフォルトは一旦削除
			Model model) {

		// ★★★ ソート条件の組み立て ★★★
		String sortProperty = "bookName"; // デフォルトのソート項目
		Sort.Direction direction = Sort.Direction.ASC; // デフォルトのソート順

		if (form.getSort() != null && !form.getSort().isEmpty()) {
			String[] sortParts = form.getSort().split(",");
			sortProperty = sortParts[0];
			if (sortParts.length > 1) {
				direction = Sort.Direction.fromString(sortParts[1]);
			}
		}

		// 新しいPageableオブジェクトを生成
		Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
				Sort.by(direction, sortProperty));

		Page<Book> bookPage = bookService.search(form, sortedPageable);

		model.addAttribute("bookPage", bookPage);
		model.addAttribute("bookSearchForm", form);

		// 検索条件をURLパラメータとして渡すための処理（変更なし）
		model.addAttribute("query", buildQuery(form));

		return "book/book_search_results";
	}

	// ページネーションのリンクで使うクエリ文字列を生成するヘルパーメソッド
	private String buildQuery(BookSearchForm form) {
		StringBuilder query = new StringBuilder();
		if (form.getTypeIds() != null) {
			for (Integer typeId : form.getTypeIds()) {
				query.append("&typeIds=").append(typeId);
			}
		}
		for (int i = 0; i < form.getCriteria().size(); i++) {
			BookSearchForm.SearchCriterion c = form.getCriteria().get(i);
			if (c.getField() != null && !c.getField().isEmpty() && c.getQuery() != null && !c.getQuery().isEmpty()) {
				query.append("&criteria[").append(i).append("].field=").append(c.getField());
				query.append("&criteria[").append(i).append("].query=").append(c.getQuery());
				query.append("&criteria[").append(i).append("].op=").append(c.getOp());
			}
		}
		return query.toString();
	}

	/**
	 * 資料詳細画面を表示
	 * (GET /book/{id})
	 */
	@GetMapping("/{id}")
	public String showDetailPage(@PathVariable("id") Integer bookId, Model model) {
		Book book = bookService.findById(bookId);
		model.addAttribute("book", book);
		return "book/book_detail";
	}

	/**
	 * 予約入力画面を表示
	 * (GET /book/{bookId}/reserve)
	 */
	@GetMapping("/{bookId}/reserve")
	public String showReservationForm(@PathVariable("bookId") Integer bookId, Model model) {
		// 表示する書籍の情報を取得
		Book book = bookService.findById(bookId);

		// フォームオブジェクトを生成し、予約対象の書籍IDをセット
		ReservationForm reservationForm = new ReservationForm();
		reservationForm.setBookId(bookId);

		model.addAttribute("book", book);
		model.addAttribute("reservationForm", reservationForm);

		return "book/book_reservation_input";
	}

	/**
	 * 予約内容を確認画面に渡す
	 * (POST /book/reserve)
	 */
	@PostMapping("/reserve")
	public String confirmReservation(@Validated @ModelAttribute ReservationForm form, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		// 会員IDの入力チェック
		if (result.hasErrors()) {
			Book book = bookService.findById(form.getBookId());
			model.addAttribute("book", book);
			return "book/book_reservation_input";
		}

		try {
			// 会員が存在するかチェック
			Member member = memberRepository.findById(form.getMemberId())
					.orElseThrow(() -> new IllegalArgumentException("入力された会員IDは存在しません。"));

			// 情報をリダイレクト先に渡す
			redirectAttributes.addFlashAttribute("reservationForm", form);
			redirectAttributes.addFlashAttribute("member", member); // 会員情報も渡す

		} catch (IllegalArgumentException e) {
			// 会員が見つからないエラー
			Book book = bookService.findById(form.getBookId());
			model.addAttribute("book", book);
			model.addAttribute("errorMessage", e.getMessage());
			return "book/book_reservation_input";
		}

		return "redirect:/book/reserve/confirm"; // 確認画面へリダイレクト
	}

	/**
     * 予約確認画面を表示
     * (GET /book/reserve/confirm)
     */
	@GetMapping("/reserve/confirm")
	public String showReservationConfirm(
	        @ModelAttribute("reservationForm") ReservationForm form,
	        @ModelAttribute("member") Member member,
	        Model model) {

	    if (form.getBookId() == null || member.getMemberId() == null) {
	        return "redirect:/book/search"; // もしくは検索画面など
	    }

	    Book book = bookService.findById(form.getBookId());
	    model.addAttribute("book", book);

	    return "book/book_reservation_confirm";
	}


	/**
	 * ★★★ 追加 ★★★
	 * 予約処理を実行
	 * (POST /book/reserve/execute)
	 */
	@PostMapping("/reserve/execute")
	public String executeReservation(@ModelAttribute ReservationForm form, RedirectAttributes redirectAttributes) {

		Reservation savedReservation = bookService.createReservation(form);

		// 完了画面に情報を渡す
		redirectAttributes.addFlashAttribute("completedReservation", savedReservation);

		return "redirect:/book/reserve/complete";
	}

	/**
	 * ★★★ 追加 ★★★
	 * 予約完了画面を表示
	 * (GET /book/reserve/complete)
	 */
	@GetMapping("/reserve/complete")
	public String showReservationComplete(Model model) {
		if (!model.containsAttribute("completedReservation")) {
			return "redirect:/"; // トップページなどへ
		}
		return "book/book_reservation_complete";
	}

	/**
     * 廃棄・紛失登録フォーム画面を表示
     */
    @PostMapping("/item/delete/form")
    public String showDisposalLossForm(@RequestParam("itemId") Integer itemId, Model model) {

        Item item = itemService.findById(itemId);

        ItemActionForm form = new ItemActionForm();
        form.setItemId(itemId);

        model.addAttribute("item", item);
        model.addAttribute("itemActionForm", form);

        return "book/book_disposal_loss_input"; // 新しいHTMLファイル
    }
 // ... (BookControllerクラス内に追加)

    /**
     * 廃棄・紛失の入力内容を検証し、確認画面へ渡す
     */
    @PostMapping("/item/delete/confirm")
    public String confirmDisposalLoss(@Validated @ModelAttribute("itemActionForm") ItemActionForm form,
                                      BindingResult result,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        // 日付や処理種別が未入力の場合のエラーチェック
        if (result.hasErrors()) {
            // エラーがあった場合、表示に必要なItem情報を再度取得して入力画面に戻る
            Item item = itemService.findById(form.getItemId());
            model.addAttribute("item", item);
            return "book/book_disposal_loss_input";
        }

        // エラーがなければ、フォーム情報をリダイレクト先に渡す
        redirectAttributes.addFlashAttribute("itemActionForm", form);
        return "redirect:/book/item/delete/confirm";
    }

    /**
     * 廃棄・紛失の確認画面を表示
     */
    @GetMapping("/item/delete/confirm")
    public String showDisposalLossConfirm(@ModelAttribute("itemActionForm") ItemActionForm form,
                                          Model model) {

        // フォーム情報からitemIdを取得し、改めてItem情報を取得して画面に渡す
        Item item = itemService.findById(form.getItemId());
        model.addAttribute("item", item);

        return "book/book_disposal_loss_confirm";
    }
    /**
     * 廃棄・紛失処理を実行
     */
    @PostMapping("/item/delete/execute") // ← 修正点1
    public String executeDisposalLoss(@ModelAttribute ItemActionForm form,
                                      RedirectAttributes redirectAttributes,
                                      Model model) { // ← 修正点2 のためにModelを追加
        try {
            Item updatedItem = itemService.processItemDisposalOrLoss(form);
            redirectAttributes.addFlashAttribute("updatedItem", updatedItem);
            redirectAttributes.addFlashAttribute("actionType", form.getActionType());

        } catch (Exception e) {
            // ★★★ 修正点2 ★★★
            // エラーが発生した場合は、リダイレクトではなく、
            // エラーメッセージと再表示用のデータをModelに詰めて、直接入力画面を返す
            model.addAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            // 表示に必要なItem情報を再度取得
            Item item = itemService.findById(form.getItemId());
            model.addAttribute("item", item);
            model.addAttribute("itemActionForm", form); // 入力値を保持したフォームを渡す
            return "book/book_disposal_loss_input";
        }

        return "redirect:/book/item/delete/complete"; // ← 修正点3
    }

    /**
     * 廃棄・紛失の完了画面を表示
     */
    @GetMapping("/item/delete/complete")
    public String showDisposalLossComplete(Model model) {
        // executeメソッドから渡された情報がない場合は、メニューへリダイレクト
        if (!model.containsAttribute("updatedItem")) {
            return "redirect:/book/menu";
        }

        // ★★★ ".html" を削除するのが正しい書き方です ★★★
        return "book/book_disposal_loss_complete";
    }

    /**
     * 貸出履歴一覧画面を表示します。
     * (GET /book/history/lending)
     */
    @GetMapping("/history/lending")
    public String showLendingHistory(
            @PageableDefault(page = 0, size = 10, sort = "lendDate", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<Lending> lendingPage = lendingService.findAllLendingHistory(pageable);
        model.addAttribute("lendingPage", lendingPage);

        return "book/history/lending_history";

    }

    /**
     * 特定の書籍に紐づく全個別資料の貸出履歴画面を表示します。
     * (GET /book/history/item/{bookId})
     */
    @GetMapping("/history/item/{bookId}")
    public String showItemHistory(@PathVariable("bookId") Integer bookId, Model model) {

        // 1. 書籍情報を取得して、画面上部に表示するためにModelに追加
        Book book = bookService.findById(bookId);
        model.addAttribute("book", book);

        // 2. この書籍の全貸出履歴を取得して、一覧表示するためにModelに追加
        List<Lending> historyList = lendingService.findLendingHistoryByBookId(bookId);
        model.addAttribute("historyList", historyList);

        return "book/book_item_history"; // 新しく作成するHTMLファイル
    }
}