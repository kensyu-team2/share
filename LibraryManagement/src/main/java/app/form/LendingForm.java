package app.form;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

public class LendingForm {
    @NotNull private Long memberId;
    @NotNull private Long itemId;
    private LocalDate lendDate = LocalDate.now();
    private LocalDate dueDate;
    // getter/setter
}