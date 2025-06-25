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
	    @Pattern(regexp = "^[0-9]*$", message = "会員IDは数字で入力してください。")
	    @Size(max = 5, message = "会員IDは５桁までで入力してください。")
	    private String memberId;

    /**
     * 貸し出す個別資料IDのリスト
     */
 // ★リストの中身に対するバリデーションを削除
	  @Size(min = 1, message = "資料IDを入力してください。")
    private List<String> itemIds;
}