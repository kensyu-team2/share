//package service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import form.BookSearchForm;
//import repository.BookRepository;
//
//@Service
//public class BookService {
//    private final BookRepository repo;
//    public BookService(BookRepository repo) { this.repo = repo; }
//
//    // ── 既存メソッド略 ──
//
//    /** 書籍検索 */
//    public List<Book> search(BookSearchForm f) {
//        return repo.findByTitleContainingAndAuthorContainingAndPublisherContaining(
//            emptyIfNull(f.getTitle()),
//            emptyIfNull(f.getAuthor()),
//            emptyIfNull(f.getPublisher())
//        );
//    }
//
//    private String emptyIfNull(String s) { return s == null ? "" : s; }
//}