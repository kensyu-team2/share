// src/main/java/com/example/library/entity/Item.java
package app.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "items")
@Data
public class Item {

    @Id
    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "get_date", nullable = false)
    private LocalDate getDate;

    @Column(name = "delete_date")
    private LocalDate deleteDate;

    @Column(name = "lost_date")
    private LocalDate lostDate;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;
}