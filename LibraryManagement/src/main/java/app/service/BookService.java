package app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Book;
import app.entity.Category;
import app.entity.Type;
import app.form.BookForm;
import app.repository.BookRepository;
import app.repository.CategoryRepository;
import app.repository.ItemRepository;
import app.repository.TypeRepository;

@Service
@Transactional // メソッド内のDB処理を一つのトランザクションとして管理します
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ItemRepository itemRepository;

    // DTOからEntityへの変換時にマスタデータを参照するため、Repositoryを注入します
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TypeRepository typeRepository;


    /**
     * 書籍を登録するメソッド
     * @param bookForm 画面から入力されたフォームデータ
     */
    public void registerBook(BookForm bookForm) {
        // ISBNの重複チェック
        if (bookRepository.findByIsbn(bookForm.getIsbn()).isPresent()) {
            // Controllerでこのエラーメッセージを掴んで画面に表示します
            throw new IllegalStateException("このISBNは既に登録されています。");
        }

        // DTOからEntityへの変換
        Book book = new Book();
        book.setIsbn(bookForm.getIsbn());
        book.setBookName(bookForm.getBookName());
        book.setAuthorName(bookForm.getAuthorName());
        book.setPublishDate(bookForm.getPublishDate());

        // IDからCategoryエンティティとTypeエンティティを取得してセットします
        Category category = categoryRepository.findById(bookForm.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("該当するカテゴリが存在しません。"));
        Type type = typeRepository.findById(bookForm.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("該当する資料区分が存在しません。"));

        book.setCategory(category);
        book.setType(type);

        // TODO: BookのIDはDB定義に基づき手動または自動で採番します
        // book.setBookId(...);

        // TODO: BookFormにない他のEntityフィールド(ruby, publisherなど)もセットする必要があります

        // 書誌情報(Book)を保存
        bookRepository.save(book);

        // TODO: 画面で入力された情報に基づき、個別資料(Item)も作成して保存します
        /*
        Item item = new Item();
        item.setBook(book);
        // ... その他のItemの情報をセット ...
        itemRepository.save(item);
        */
    }
}