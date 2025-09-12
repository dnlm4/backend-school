package org.softfisticado.infrastructure.adapters.output.repository;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.softfisticado.domain.model.Student;
import org.softfisticado.domain.repository.StudentRepository;
import org.softfisticado.infrastructure.mapper.StudentMapper;
import org.softfisticado.infrastructure.persistence.entity.StudentEntity;
import org.softfisticado.infrastructure.persistence.repository.StudentCrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ApplicationScoped
public class StudentRepositoryImpl implements StudentRepository {

    @Inject
    PgPool pgPool;

    @Inject
    StudentCrudRepository studentCrudRepository;

    @Override
    public Uni save(Student student) {
        StudentEntity studentEntity = StudentMapper.toStudentEntity(student);
        studentEntity.setDateCreated(LocalDate.from(LocalDateTime.now()));
        return studentCrudRepository.save(studentEntity);
    }

    @Override
    public Uni update(Student student) {
        StudentEntity studentEntity = StudentMapper.toStudentEntity(student);
        studentEntity.setDateModified(LocalDate.from(LocalDateTime.now()));
        return studentCrudRepository.update(studentEntity);
    }

    @Override
    public Uni delete(Long id) {
        return studentCrudRepository.delete(id,new StudentEntity());
    }

    @Override
    public Uni findById(Long id) {
        return studentCrudRepository.findById(id,new StudentEntity());
    }
}
