package app.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "status")
@Data
public class Status {
    @Id
    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "status_name", nullable = false, length = 10)
    private String statusName;

    @OneToMany(mappedBy = "status")
    private List<Item> items;
}