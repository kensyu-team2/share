package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import entity.Item;

@Repository
public interface MemberRepository extends JpaRepository<Item, Integer> {

}
