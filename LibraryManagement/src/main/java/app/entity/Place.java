package app.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "place")
@Data
public class Place {
    @Id
    @Column(name = "place_id", length = 20)
    private String placeId;

    @Column(name = "place_name", nullable = false, length = 40)
    private String placeName;

    @OneToMany(mappedBy = "place")
    private List<Item> items;
}