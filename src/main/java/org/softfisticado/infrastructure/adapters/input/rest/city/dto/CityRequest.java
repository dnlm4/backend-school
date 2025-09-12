package org.softfisticado.infrastructure.adapters.input.rest.city.dto;

public class CityRequest {
    private String name;

    public CityRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
