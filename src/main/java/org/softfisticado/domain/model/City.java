package org.softfisticado.domain.model;

public class City {
    private Long id;
    private String name;

    public City() {
    }

    public City(Long id) {
        this.id = id;
    }

    public City(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public City(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
