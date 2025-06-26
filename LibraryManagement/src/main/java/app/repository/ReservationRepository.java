package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import app.entity.Book;
import app.entity.Reservation;

@Repository

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @EntityGraph(attributePaths = {"book"})
    List<Reservation> findByMember_MemberId(Integer memberId);

    /**
     * 特定の書籍(Book)に紐づく全ての予約情報を、予約日が古い順で取得します。
     * @param book 検索対象の書籍エンティティ
     * @return 予約エンティティのリスト
     */
    List<Reservation> findByBookOrderByReserveDateAsc(Book book);

    @Query("SELECT r FROM Reservation r WHERE EXISTS (" +
    	       "SELECT i FROM Item i WHERE i.book = r.book AND i.status.statusId = 3)")
    List<Reservation> findReservedOnly();

}

