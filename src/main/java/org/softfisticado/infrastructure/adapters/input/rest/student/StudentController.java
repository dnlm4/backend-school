package org.softfisticado.infrastructure.adapters.input.rest.student;


import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.softfisticado.application.usecases.StudentUseCase;
import org.softfisticado.domain.model.Student;
import org.softfisticado.infrastructure.adapters.input.rest.student.dto.StudentRequest;
import org.softfisticado.infrastructure.mapper.StudentMapper;

@Path("/student")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "STUDENT")
@NonBlocking
@ApplicationScoped
public class StudentController {

    @Inject
    StudentUseCase  studentUseCase;

    @POST
    public Uni<Response> save(StudentRequest request) {
        Student student = StudentMapper.toStudent(request);
        return studentUseCase.save(student)
                .onItem()
                .transform(saved->{
                    return saved;
                });
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id")Long id,StudentRequest request) {
        Student student = StudentMapper.toStudent(request);
        return studentUseCase.update(id,student)
                .onItem()
                .transform(updated->{
                    return updated;
                });
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id")Long id) {
        return studentUseCase.delete(id)
                .onItem()
                .transform(deleted->{
                    return deleted;
                });
    }
    @GET
    @Path("{id}")
    public Uni<Response> findById(@PathParam("id")Long id) {
        return studentUseCase.findById(id);
    }

    @GET
    @Path("all")
    public Multi<Response> findAll() {
        return studentUseCase.findAll();
    }
}
