package org.softfisticado.domain.repository;

import io.smallrye.mutiny.Uni;
import org.softfisticado.domain.model.City;

public interface CityRepository {
    Uni save(City city);
    Uni update(City city);
    Uni delete(Long id);

    Uni findById(Long id);
}
