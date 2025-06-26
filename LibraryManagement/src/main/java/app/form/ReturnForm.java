package app.form;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ReturnForm {

    /**
     * 返却する個別資料IDのリスト (最大10個)
     */
	@NotNull
	@Size(min = 1, message = "返却する資料を1冊以上入力してください。")
	private List<String> itemIds;

}