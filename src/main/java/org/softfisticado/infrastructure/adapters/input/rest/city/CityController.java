package org.softfisticado.infrastructure.adapters.input.rest.city;

import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.softfisticado.application.usecases.CityUseCase;
import org.softfisticado.infrastructure.adapters.input.rest.city.dto.CityRequest;


@Path("/city")
@Tag(name = "CITY")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@NonBlocking
@ApplicationScoped
public class CityController {
    @Inject
    CityUseCase cityUseCase;

    @POST
    public Uni<Response> save(CityRequest request) {
        return cityUseCase.save(request)
                .onItem()
                .transform(saved->{
                    return saved;
                });
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(@PathParam("id")Long id,CityRequest request) {
        return cityUseCase.update(id,request)
                .onItem()
                .transform(updated-> {
                    return updated;
                });
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(@PathParam("id")Long id) {
        return cityUseCase.delete(id)
                .onItem()
                .transform(deleted->{
                    return  deleted;
                });
    }

    @GET
    @Path("{id}")
    public Uni<Response> findById(@PathParam("id")Long id) {
        return cityUseCase.findById(id);
    }

}
