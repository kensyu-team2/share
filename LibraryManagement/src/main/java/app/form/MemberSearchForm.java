package app.form;

import lombok.Data;

@Data
public class MemberSearchForm {
    private String keyword; // 氏名・ID・メールなど部分一致
}
