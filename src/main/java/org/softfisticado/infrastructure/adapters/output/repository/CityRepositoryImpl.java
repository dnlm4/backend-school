package org.softfisticado.infrastructure.adapters.output.repository;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.softfisticado.domain.model.City;
import org.softfisticado.domain.repository.CityRepository;
import org.softfisticado.infrastructure.mapper.CityMapper;
import org.softfisticado.infrastructure.persistence.entity.CityEntity;
import org.softfisticado.infrastructure.persistence.repository.CityCrudRepository;

@ApplicationScoped
public class CityRepositoryImpl implements CityRepository {

    @Inject
    CityCrudRepository cityCrudRepository;

    @Override
    public Uni save(City city) {
        CityEntity cityEntity = CityMapper.toCityEntity(city);
        return cityCrudRepository.save(cityEntity);
    }

    @Override
    public Uni update(City city) {
        CityEntity cityEntity = CityMapper.toCityEntity(city);
        return cityCrudRepository.update(cityEntity);
    }

    @Override
    public Uni delete(Long id) {
        return cityCrudRepository.delete(id, new CityEntity());
    }

    @Override
    public Uni findById(Long id) {
        return cityCrudRepository.findById(id,new CityEntity());
    }


}
