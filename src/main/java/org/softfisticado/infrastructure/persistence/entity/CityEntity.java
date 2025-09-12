package org.softfisticado.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@Entity
@Table(name = "city")
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public CityEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CityEntity(String name) {
        this.name = name;
    }

    public CityEntity(Long id) {
        this.id = id;
    }
}
