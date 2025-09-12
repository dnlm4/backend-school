package org.softfisticado.application.usecases;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.softfisticado.domain.model.City;
import org.softfisticado.domain.repository.CityRepository;
import org.softfisticado.infrastructure.adapters.input.rest.city.dto.CityRequest;
import org.softfisticado.infrastructure.mapper.CityMapper;

@RequestScoped
public class CityUseCase {
    @Inject
    private CityRepository cityRepository;

    public Uni save(CityRequest request) {
        City city = CityMapper.toCity(request);
        return cityRepository.save(city);
    }

    public Uni update(Long id,CityRequest request) {
        City city = CityMapper.toCity(request);
        city.setId(id);
        return cityRepository.update(city);
    }

    public Uni delete(Long id) {
        return cityRepository.delete(id);
    }

    public Uni findById(Long id) {
        return cityRepository.findById(id);
    }
}
