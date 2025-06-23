package app.form;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LendingForm {
    /**
     * 会員ID
     * 必須入力とし、数値であることを検証します。
     * メッセージは messages.properties で定義します。
     */
    @NotNull
    @Digits(integer = 5, fraction = 0)
    @Min(value = 1)
    private Integer memberId;
    /**
     * 個別資料ID
     * 必須入力とし、数値であることを検証します。
     * メッセージは messages.properties で定義します。
     */
    @NotNull
    @Digits(integer = 10, fraction = 0)
    @Min(value = 1)
    private Integer itemId;
}











