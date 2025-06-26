// src/main/java/app/repository/ItemRepository.java
package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	 long countByBookBookIdAndStatusStatusId(Integer bookId, Integer statusId);
    // 今は基本的なメソッドだけで十分です
}