// src/main/java/app/form/BookForm.java
package app.form; // パッケージ名を app.form に修正

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class BookForm {
    // ... (クラスの中身は変更なし) ...

    @NotEmpty(message = "ISBNを入力してください。")
    @Size(max = 20, message = "ISBNは20桁以内で入力してください。")
    private String isbn;

    @NotEmpty(message = "書籍名を入力してください。")
    @Size(max = 40, message = "書籍名は40桁以内で入力してください。")
    private String bookName;

    @NotEmpty
    @Size(max = 20)
    private String authorName;

    @NotNull(message = "出版年月日を入力してください。")
    private LocalDate publishDate;

    @NotNull(message = "ジャンルを選択してください。")
    private Integer categoryId;

    @NotNull(message = "資料区分を選択してください。")
    private Integer typeId;
}