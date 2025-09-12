package org.softfisticado.infrastructure.mapper;

import org.softfisticado.domain.model.City;
import org.softfisticado.infrastructure.adapters.input.rest.city.dto.CityRequest;
import org.softfisticado.infrastructure.adapters.input.rest.city.dto.CityResponse;
import org.softfisticado.infrastructure.persistence.entity.CityEntity;

public class CityMapper {
    public static City toCity(CityRequest request) {
        return new City(request.getName());
    }
    public static City cityEntityToCity(CityEntity entity) {
        return new City(entity.getId(),entity.getName());
    }
    public static CityResponse toCityResponse(City city) {
        return new CityResponse(city.getId(), city.getName());
    }
    public static CityEntity toCityEntity(City city) {
        return  new CityEntity(city.getId(),city.getName());
    }


}
