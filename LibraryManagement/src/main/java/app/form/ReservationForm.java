package app.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReservationForm {

    @NotNull
    private Integer bookId; // 予約対象の書籍ID

    @NotNull(message = "会員IDを入力してください。")
    private Integer memberId; // 予約する会員のID
}