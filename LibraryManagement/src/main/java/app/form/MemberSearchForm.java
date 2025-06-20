package com.example.library.web.form;

import com.example.library.service.dto.MemberSearchCriteria;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberSearchForm {
    private String name;
    private String kana;
    private Boolean active; // 在籍中／退会済み

    /** Form → 検索条件 DTO 変換 */
    public MemberSearchCriteria toCriteria() {
        MemberSearchCriteria c = new MemberSearchCriteria();
        c.setName(name);
        c.setKana(kana);
        c.setActive(active);
        return c;
    }
}