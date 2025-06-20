// src/main/java/app/form/BookForm.java
package app.form;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class BookForm {

    @NotEmpty(message = "ISBNを入力してください。")
    @Size(max = 20, message = "ISBNは20桁以内で入力してください。")
    private String isbn;

    @NotEmpty(message = "書籍名を入力してください。")
    @Size(max = 40, message = "書籍名は40桁以内で入力してください。")
    private String bookName;

    // --- ここから追加 ---
    @NotEmpty(message = "書籍名フリガナを入力してください。")
    @Size(max = 40, message = "書籍名フリガナは40桁以内で入力してください。")
    private String bookRuby;
    // --- ここまで追加 ---

    @NotEmpty(message = "著者名を入力してください。")
    @Size(max = 20, message = "著者名は20桁以内で入力してください。")
    private String authorName;

    // --- ここから追加 ---
    @NotEmpty(message = "著者名フリガナを入力してください。")
    @Size(max = 20, message = "著者名フリガナは20桁以内で入力してください。")
    private String authorRuby;
    // --- ここまで追加 ---
    @NotEmpty(message = "出版社を入力してください。")
    @Size(max = 40, message = "出版社は40桁以内で入力してください。")
    private String publisher;

    @NotNull(message = "出版年月日を入力してください。")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    @NotNull(message = "入荷年月日を入力してください。")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate arrivalDate;

    @NotNull(message = "ジャンルを選択してください。")
    private Integer categoryId;

    @NotNull(message = "資料区分を選択してください。")
    private Integer typeId;

    // publisher や keyword など、他のフォーム項目も必要に応じて追加します
}