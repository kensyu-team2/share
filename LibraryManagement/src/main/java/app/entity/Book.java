// src/main/java/com/example/library/entity/Book.java
package app.entity;

import java.time.LocalDate;

// import文を jakarta から javax に修正
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue; // ← import文を追加
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "books")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "isbn", nullable = false, length = 20)
    private String isbn;

    @Column(name = "book_name", nullable = false, length = 40)
    private String bookName;

    @Column(name = "book_ruby", nullable = false, length = 40)
    private String bookRuby;

    @Column(name = "author_name", nullable = false, length = 20)
    private String authorName;

    @Column(name = "author_ruby", nullable = false, length = 20)
    private String authorRuby;

    @Column(name = "publisher", nullable = false, length = 40)
    private String publisher;

    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;


}