// src/main/java/app/repository/BookRepository.java
package app.repository;

import app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    /**
     * ISBNをキーにBookを検索します。
     * @param isbn 検索するISBN
     * @return 該当するBook（存在しない場合は空のOptional）
     */
    Optional<Book> findByIsbn(String isbn);
}