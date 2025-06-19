package form;

import javax.validation.constraints.NotBlank;

public class MemberForm {
    private Long id;

    @NotBlank(message="名前は必須です")
    private String name;

    private String phone;

    // getter/setter
}