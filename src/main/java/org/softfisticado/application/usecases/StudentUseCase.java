package org.softfisticado.application.usecases;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.softfisticado.domain.model.Student;
import org.softfisticado.domain.repository.StudentRepository;

@RequestScoped
public class StudentUseCase {
    @Inject
    private StudentRepository studentRepository;

    public Uni save(Student student) {
        return studentRepository.save(student);
    }

    public Uni update(Long id,Student student) {
        student.setId(id);
        return studentRepository.update(student);
    }
    public Uni delete(Long id) {
        return studentRepository.delete(id);
    }
    public Uni findById(Long id) {
        return studentRepository.findById(id);
    }

}
