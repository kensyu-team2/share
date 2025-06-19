package app.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="status")
public class Status {
    @Id private Integer id;
    private String name;
    // getter/setter
}