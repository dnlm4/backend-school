package org.softfisticado.shared.utils;

import io.smallrye.mutiny.Uni;


public interface CrudRepository<E> {
    Uni save(E entity);
    Uni update(E entity);
    Uni delete(Long id,E entity);

    Uni findById(Long id,E entity);

}
