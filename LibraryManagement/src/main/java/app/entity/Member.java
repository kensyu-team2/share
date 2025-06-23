package app.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "members")
@Data
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "name", nullable = false, length = 20)
    private String name;

    @Column(name = "name_ruby", nullable = false, length = 40)
    private String nameRuby;

    @Column(name = "address", nullable = false, length = 40)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "mail", nullable = false, length = 40)
    private String mail;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "retire_date")
    private LocalDate retireDate;

    // この会員が行った貸出一覧
    @OneToMany(mappedBy = "member")
    private List<Lending> lendings;

    // この会員が行った予約一覧
    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations;
}