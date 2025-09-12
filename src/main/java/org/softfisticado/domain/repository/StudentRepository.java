package org.softfisticado.domain.repository;

import io.smallrye.mutiny.Uni;
import org.softfisticado.domain.model.Student;

public interface StudentRepository {
    Uni save(Student student);
    Uni update(Student student);
    Uni delete(Long id);
    Uni findById(Long id);
}
