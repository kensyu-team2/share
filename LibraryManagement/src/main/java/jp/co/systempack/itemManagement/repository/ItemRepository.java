package jp.co.systempack.itemManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.systempack.itemManagement.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

}
