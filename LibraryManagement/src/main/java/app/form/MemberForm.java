package app.form;

import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MemberForm {

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String nameRuby;

    @NotBlank
    @Size(max = 40)
    private String address;

    @NotBlank
    @Size(max = 20)
    private String telNumber;

    @NotBlank
    @Email
    @Size(max = 40)
    private String email;

    @NotNull
    private LocalDate birthday;
}
