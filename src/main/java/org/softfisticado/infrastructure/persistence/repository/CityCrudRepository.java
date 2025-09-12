package org.softfisticado.infrastructure.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.softfisticado.domain.model.City;
import org.softfisticado.infrastructure.persistence.entity.CityEntity;
import org.softfisticado.infrastructure.persistence.jpa.GenericCrudRepository;
import org.softfisticado.shared.utils.CrudRepository;

@ApplicationScoped
public class CityCrudRepository extends GenericCrudRepository<CityEntity> implements CrudRepository<CityEntity> {
}
