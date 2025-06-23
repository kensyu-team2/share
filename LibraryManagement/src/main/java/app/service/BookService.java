package app.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; // 追加
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Book;
import app.entity.Category;
import app.entity.Item;
import app.entity.Place;
import app.entity.Status;
import app.entity.Type;
import app.form.BookForm;
import app.form.BookSearchForm; // 追加
import app.repository.BookRepository;
import app.repository.CategoryRepository;
import app.repository.ItemRepository;
import app.repository.PlaceRepository;
import app.repository.StatusRepository;
import app.repository.TypeRepository;

@Service
@Transactional
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TypeRepository typeRepository;

	// ItemでPlaceとStatusも使うため、リポジトリを追加
	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private StatusRepository statusRepository;

	public void registerBook(BookForm bookForm) {
		// ISBNの重複チェック
		if (bookRepository.findByIsbn(bookForm.getIsbn()).isPresent()) {
			throw new IllegalStateException("このISBNは既に登録されています。");
		}

		// --- Bookエンティティの作成と保存 ---
		Book book = new Book();
		book.setIsbn(bookForm.getIsbn());
		book.setBookName(bookForm.getBookName());
		book.setAuthorName(bookForm.getAuthorName());
		book.setPublishDate(bookForm.getPublishDate());
		book.setBookRuby(bookForm.getBookRuby());
		book.setAuthorRuby(bookForm.getAuthorRuby());
		book.setPublisher(bookForm.getPublisher());
		// TODO: DTOに項目を追加し、rubyやpublisherなどもセットする
		Book savedBook = bookRepository.save(book);

		// --- Itemエンティティの作成と保存 ---
		Item item = new Item();
		item.setBook(savedBook);
		item.setGetDate(LocalDate.now());

		// 各マスタテーブルからIDに対応するエンティティを取得してセット
		Category category = categoryRepository.findById(bookForm.getCategoryId())
				.orElseThrow(() -> new IllegalArgumentException("該当するカテゴリが存在しません。"));
		Type type = typeRepository.findById(bookForm.getTypeId())
				.orElseThrow(() -> new IllegalArgumentException("該当する資料区分が存在しません。"));

		// 仮でデフォルト値を設定（本来はフォームから受け取るか、別のロジックで決定）
		Place place = placeRepository.findById("1F-A-1")
				.orElseThrow(() -> new IllegalArgumentException("該当する配架場所が存在しません。"));
		Status status = statusRepository.findById(1) // 1:在庫あり
				.orElseThrow(() -> new IllegalArgumentException("該当するステータスが存在しません。"));

		item.setCategory(category);
		item.setType(type);
		item.setPlace(place);
		item.setStatus(status);

		itemRepository.save(item);
	}

	//search
	// 引数に「Pageable pageable」を追加します。
	public Page<Book> search(BookSearchForm form, Pageable pageable) {
		Specification<Book> spec = buildSpecification(form);
		return bookRepository.findAll(spec, pageable);
	}

	private Specification<Book> buildSpecification(BookSearchForm form) {

		Specification<Book> spec = Specification.where(null);

		// キーワード検索条件の組み立て
		for (BookSearchForm.SearchCriterion criterion : form.getCriteria()) {
			if (criterion.getField() != null && !criterion.getField().isEmpty() &&
					criterion.getQuery() != null && !criterion.getQuery().isEmpty()) {
				Specification<Book> nextSpec;

				// 個別資料ID(itemId)での検索は、JOINが必要なため特別扱い
				if ("itemId".equals(criterion.getField())) {
					nextSpec = (root, query, cb) -> {
						try {
							// 文字列を整数に変換して検索
							Integer itemId = Integer.parseInt(criterion.getQuery());
							return cb.equal(root.join("items").get("itemId"), itemId);
						} catch (NumberFormatException e) {
							// 数字でなければ条件に合致しないようにする
							return cb.disjunction();
						}
					};
				} else {
					// それ以外の通常のフィールドでの検索
					nextSpec = (root, query, cb) -> {
						switch (criterion.getMatch()) {
						case "startsWith":
							return cb.like(root.get(criterion.getField()), criterion.getQuery() + "%");
						case "endsWith":
							return cb.like(root.get(criterion.getField()), "%" + criterion.getQuery());
						case "equals":
							return cb.equal(root.get(criterion.getField()), criterion.getQuery());
						case "contains":
						default:
							return cb.like(root.get(criterion.getField()), "%" + criterion.getQuery() + "%");
						}
					};
				}
				// 演算子(op)に応じて、これまでの条件(spec)と結合する
				if ("OR".equalsIgnoreCase(criterion.getOp())) {
					spec = spec.or(nextSpec);
				} else if ("NOT".equalsIgnoreCase(criterion.getOp())) {
					// NOT条件の場合は、その条件を否定してANDで結合
					spec = spec.and(Specification.not(nextSpec));
				} else { // AND または 未指定の場合
					spec = spec.and(nextSpec);
				}

				if ("OR".equalsIgnoreCase(criterion.getOp())) {
					spec = spec.or(nextSpec);
				} else { // AND または 未指定の場合
					spec = spec.and(nextSpec);
				}
			}
		}

		// 資料区分での絞り込み条件（変更なし）
		if (form.getTypeIds() != null && !form.getTypeIds().isEmpty()) {
			// ... (省略)
		}

		return spec;
	}
	public Book findById(Integer bookId) {
        return bookRepository.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("該当する書籍が見つかりません。"));
    }
}