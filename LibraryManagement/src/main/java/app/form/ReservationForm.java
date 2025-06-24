package app.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;


public class ReservationForm {

    /** 予約する会員 */
    @NotNull(message="会員を選択してください")
    private Integer memberId;  // Long → Integer に変更

    /** 予約する書籍（書誌） */
    @NotNull(message="書籍を選択してください")
    private Integer bookId;  // Long → Integer に変更

    /** 予約日（デフォルトで today をセットしておく） */
    private LocalDate reserveDate = LocalDate.now();

    public Integer getMemberId() {
        return memberId;
    }
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getBookId() {
        return bookId;
    }
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public LocalDate getReserveDate() {
        return reserveDate;
    }
    public void setReserveDate(LocalDate reserveDate) {
        this.reserveDate = reserveDate;
    }
}

//    @NotNull
//    private Integer bookId; // 予約対象の書籍ID

//    @NotNull(message = "会員IDを入力してください。")
//    private Integer memberId; // 予約する会員のID
//

