package app.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @NotBlank(message = "それぞれの要素（氏名）は文字列を入力してください。")
    @Size(max = 20, message = "氏名は20桁以内で入力してください。")
    @Column(nullable = false, length = 20)
    private String name;

    @NotBlank(message = "それぞれの要素（氏名フリガナ）は文字列を入力してください。")
    @Size(max = 40, message = "氏名フリガナは40桁以内で入力してください。")
    @Column(name = "name_ruby", nullable = false, length = 40)
    private String nameRuby;

    @NotBlank(message = "住所は文字列を入力してください。")
    @Size(max = 40, message = "住所は40桁以内で入力してください。")
    @Column(nullable = false, length = 40)
    private String address;

    @NotBlank(message = "電話番号は文字列で入力してください。")
    @Size(max = 20, message = "電話番号は20桁までで入力してください。")
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phone;

    @NotBlank(message = "メールは文字列を入力してください。")
    @Size(max = 40, message = "メールは40桁以内で入力してください。")
    @Email(message = "メールアドレスの形式が正しくありません。")
    @Column(name = "mail", nullable = false, unique = true, length = 40)
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "retire_date")
    private LocalDate retireDate;
}
