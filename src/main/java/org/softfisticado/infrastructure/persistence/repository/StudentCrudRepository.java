package org.softfisticado.infrastructure.persistence.repository;

import jakarta.enterprise.context.ApplicationScoped;
import org.softfisticado.domain.model.Student;
import org.softfisticado.infrastructure.persistence.entity.StudentEntity;
import org.softfisticado.infrastructure.persistence.jpa.GenericCrudRepository;
import org.softfisticado.shared.utils.CrudRepository;

@ApplicationScoped
public class StudentCrudRepository extends GenericCrudRepository<StudentEntity>  implements CrudRepository<StudentEntity> {
}
