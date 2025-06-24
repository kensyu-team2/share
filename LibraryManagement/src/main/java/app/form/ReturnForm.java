package app.form;

import java.util.List;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ReturnForm {

    /**
     * 返却する個別資料IDのリスト (最大10個)
     */
    @Size(min = 1, max = 10, message = "返却する資料を1〜10冊まで入力してください。")
    private List<String> itemIds;
}