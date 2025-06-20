package app.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String kana;

    @NotBlank
    @Pattern(regexp = "\\d{10,11}")
    private String tel;

    @NotBlank
    @Size(max = 100)
    private String address;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    @AssertTrue(message = "本人確認が必要です")
    private boolean verified;

    /** Form → Entity 変換 */
    public Member toMember() {
        Member m = new Member();
        m.setId(this.id);
        m.setName(name);
        m.setKana(kana);
        m.setTel(tel);
        m.setAddress(address);
        m.setEmail(email);
        return m;
    }

    /** Entity → Form 変換 */
    public static MemberForm from(Member m) {
        MemberForm f = new MemberForm();
        f.setId(m.getId());
        f.setName(m.getName());
        f.setKana(m.getKana());
        f.setTel(m.getTel());
        f.setAddress(m.getAddress());
        f.setEmail(m.getEmail());
        f.setVerified(true);
        return f;
    }
}