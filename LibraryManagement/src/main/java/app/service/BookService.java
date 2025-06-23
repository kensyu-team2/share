package app.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.entity.Book;
import app.entity.Category;
import app.entity.Item;
import app.entity.Member; // ← 正しいMemberクラス
import app.entity.Place;
import app.entity.Reservation;
import app.entity.Status;
import app.entity.Type;
import app.form.BookForm;
import app.form.BookSearchForm;
import app.form.ReservationForm;
import app.repository.BookRepository;
import app.repository.CategoryRepository;
import app.repository.ItemRepository;
import app.repository.MemberRepository;
import app.repository.PlaceRepository;
import app.repository.ReservationRepository;
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

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    public Book registerBook(BookForm bookForm) {
        if (bookRepository.findByIsbn(bookForm.getIsbn()).isPresent()) {
            throw new IllegalStateException("このISBNは既に登録されています。");
        }

        Book book = new Book();
        book.setIsbn(bookForm.getIsbn());
        book.setBookName(bookForm.getBookName());
        book.setBookRuby(bookForm.getBookRuby());
        book.setAuthorName(bookForm.getAuthorName());
        book.setAuthorRuby(bookForm.getAuthorRuby());
        book.setPublisher(bookForm.getPublisher());
        book.setPublishDate(bookForm.getPublishDate());

        Book savedBook = bookRepository.save(book);

        Item item = new Item();
        item.setBook(savedBook);
        item.setGetDate(bookForm.getArrivalDate());

        Category category = categoryRepository.findById(bookForm.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("該当するカテゴリが存在しません。"));
        Type type = typeRepository.findById(bookForm.getTypeId())
                .orElseThrow(() -> new IllegalArgumentException("該当する資料区分が存在しません。"));
        Place place = placeRepository.findById("1F-A-1")
                .orElseThrow(() -> new IllegalArgumentException("該当する配架場所が存在しません。"));
        Status status = statusRepository.findById(1) // 1:在庫あり
                .orElseThrow(() -> new IllegalArgumentException("該当するステータスが存在しません。"));

        item.setCategory(category);
        item.setType(type);
        item.setPlace(place);
        item.setStatus(status);

        itemRepository.save(item);

        return savedBook; // 保存したBookオブジェクトを返す
    }

    public Page<Book> search(BookSearchForm form, Pageable pageable) {
        Specification<Book> spec = buildSpecification(form);
        return bookRepository.findAll(spec, pageable);
    }

    private Specification<Book> buildSpecification(BookSearchForm form) {
        Specification<Book> spec = Specification.where(null);

        for (BookSearchForm.SearchCriterion criterion : form.getCriteria()) {
            if (criterion.getField() != null && !criterion.getField().isEmpty() &&
                criterion.getQuery() != null && !criterion.getQuery().isEmpty()) {

                Specification<Book> nextSpec;

                if ("itemId".equals(criterion.getField())) {
                    nextSpec = (root, query, cb) -> {
                        try {
                            Integer itemId = Integer.parseInt(criterion.getQuery());
                            return cb.equal(root.join("items").get("itemId"), itemId);
                        } catch (NumberFormatException e) {
                            return cb.disjunction();
                        }
                    };
                } else {
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

                if ("OR".equalsIgnoreCase(criterion.getOp())) {
                    spec = spec.or(nextSpec);
                } else if ("NOT".equalsIgnoreCase(criterion.getOp())) {
                    spec = spec.and(Specification.not(nextSpec));
                } else {
                    spec = spec.and(nextSpec);
                }
            }
        }

        if (form.getTypeIds() != null && !form.getTypeIds().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.join("items").get("type").get("typeId").in(form.getTypeIds()));
        }

        if (form.getCategoryIds() != null && !form.getCategoryIds().isEmpty()) {
            spec = spec.and((root, query, cb) -> root.join("items").get("category").get("categoryId").in(form.getCategoryIds()));
        }

        return spec;
    }

    public Book findById(Integer bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("該当する書籍が見つかりません。"));
    }

    public Reservation createReservation(ReservationForm form) {
        Book book = bookRepository.findById(form.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("該当する書籍が見つかりません。"));
        Member member = memberRepository.findById(form.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("該当する会員が見つかりません。"));

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setMember(member);
        reservation.setReserveDate(LocalDate.now());

        return reservationRepository.save(reservation);
    }
}