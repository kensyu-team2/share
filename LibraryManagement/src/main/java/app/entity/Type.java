// src/main/java/com/example/library/entity/Type.java
package app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "type")
@Data
public class Type {
    @Id
    @Column(name = "type_id")
    private Integer typeId;

    @Column(name = "type_name", nullable = false, length = 20)
    private String typeName;
}