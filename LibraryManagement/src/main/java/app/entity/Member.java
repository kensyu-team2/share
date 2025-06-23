package app.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private int memberId;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(name = "name_ruby", nullable = false, length = 40)
    private String nameRuby;

    @Column(nullable = false, length = 40)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phone;

    @Column(name = "mail", nullable = false, unique = true, length = 40)
    private String email;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "retire_date")
    private LocalDate retireDate;
}
