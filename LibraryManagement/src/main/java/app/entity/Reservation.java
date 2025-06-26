package app.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "reservation")
@Data
public class Reservation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reserveId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "reserve_date", nullable = false)
    private LocalDate reserveDate;

//    public int getReserveId() {
//        return reserveId;
//    }

//    public void setReserveId(int reserveId) {
//        this.reserveId = reserveId;
//    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getReserveDate() {
        return reserveDate;
    }

    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }

    public LocalDate getDeadline() {
        if (reserveDate == null) {
            return null;
        }
        return reserveDate.plusDays(10);  // 例：予約日の14日後を取り置き期限とする
    }
}