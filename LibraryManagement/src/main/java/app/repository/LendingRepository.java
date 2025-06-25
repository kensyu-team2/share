package app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import app.entity.Item;
import app.entity.Lending;
import app.entity.Member;

public interface LendingRepository extends JpaRepository<Lending, Integer>, JpaSpecificationExecutor<Lending> { // ★ LongからIntegerに修正
    long countByMemberAndReturnDateIsNull(Member member);
    List<Lending> findByMemberAndReturnDateIsNull(Member member);
    Optional<Lending> findByItemAndReturnDateIsNull(Item item);
    /**
     * 特定の書籍(bookId)に紐づく全ての個別資料の、全貸出履歴を取得します。
     * 貸出日の降順でソートします。
     * @param bookId 検索対象の書籍ID
     * @return 貸出エンティティのリスト
     */
    List<Lending> findByItem_Book_BookIdOrderByLendDateDesc(Integer bookId);

    @Query("SELECT l FROM Lending l WHERE l.returnDate IS NULL AND l.dueDate < :today")
    List<Lending> findOverdueLendings(LocalDate today);

}