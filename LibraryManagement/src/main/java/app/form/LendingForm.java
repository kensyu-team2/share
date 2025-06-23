// src/main/java/app/form/LendingForm.java
package app.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 貸出画面の入力フォームと対応するDTO(Data Transfer Object)
 */
@Data
public class LendingForm {

    /**
     * 会員ID
     */
    @NotBlank(message = "会員IDを入力してください。")
    @Pattern(regexp = "^[0-9]{1,5}$", message = "会員IDは5桁までの数字で入力してください。")
    private String memberId;

    /**
     * 貸し出す個別資料IDのリスト
     */
    @Size(min = 1, message = "貸出希望の資料を1冊以上入力してください。")
    private List<
                @NotBlank(message = "資料IDを入力してください。")
                @Pattern(regexp = "^[0-9]{1,10}$", message = "個別資料IDは10桁までの数字で入力してください。")
                String
            > itemIds;

}