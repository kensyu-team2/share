package form;

import javax.validation.constraints.NotBlank;

public class BookForm {
    private Long id;

    @NotBlank(message="タイトルは必須です")
    private String title;

    private String author;
    private String publisher;
    // getter/setter
}