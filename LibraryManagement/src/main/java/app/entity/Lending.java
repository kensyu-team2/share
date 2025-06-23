package app.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="lending")
public class Lending {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne @JoinColumn(name="item_id")
    private Item item;

    private LocalDate lendDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    // getter/setter
}