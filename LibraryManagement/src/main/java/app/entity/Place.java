package app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "place")
@Data
public class Place {

    @Id
    @Column(name = "place_id", nullable = false, length = 20)
    private String placeId;

    @Column(name = "place_name", nullable = false, length = 40)
    private String placeName;
}