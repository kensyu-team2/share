package app.form;

public class MemberSearchForm {
    /** 会員名（あいまい検索） */
    private String name;

    /** 電話番号（あいまい or 完全一致） */
    private String phone;

    // 期間検索が必要なら以下を追加
    // private LocalDate registeredFrom;
    // private LocalDate registeredTo;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}