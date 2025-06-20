package app.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Book;
import app.entity.Category;
import app.entity.Item;
import app.entity.Place;
import app.entity.Status;
import app.entity.Type;
import app.form.BookForm;
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
}