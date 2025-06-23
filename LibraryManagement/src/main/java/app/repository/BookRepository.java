// src/main/java/app/repository/BookRepository.java
package app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // 追加

import app.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> { // JpaSpecificationExecutor を追加
    Optional<Book> findByIsbn(String isbn);
}