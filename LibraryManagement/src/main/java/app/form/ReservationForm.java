package app.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class ReservationForm {
    /** 予約する会員 */
    @NotNull(message="会員を選択してください")
    private Long memberId;

    /** 予約する書籍（書誌） */
    @NotNull(message="書籍を選択してください")
    private Long bookId;

    /** 予約日（デフォルトで today をセットしておく） */
    private LocalDate reserveDate = LocalDate.now();

    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReserveDate() {
        return reserveDate;
    }
    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }
}