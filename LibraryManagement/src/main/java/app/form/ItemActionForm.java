package app.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ItemActionForm {

    @NotNull
    private Integer itemId; // 対象となる個別資料ID

    @NotNull(message = "日付を入力してください。")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate actionDate; // 廃棄日または紛失日

    @NotNull(message = "処理を選択してください。")
    private String actionType; // "dispose" (廃棄) または "lose" (紛失)
}