package com.sixpointsix.pothole.api.resources;

import com.google.common.base.Optional;
import com.sixpointsix.pothole.api.domains.Pothole;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.mongojack.ObjectId;
import org.mongojack.WriteResult;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/potholes")
@Produces(MediaType.APPLICATION_JSON)
public class PotholeResource {
    private JacksonDBCollection<Pothole, String> potholeCollection;

    public PotholeResource(){

    }

    public PotholeResource(
        JacksonDBCollection<Pothole, String> potholeCollection) {
            this.potholeCollection = potholeCollection;
    }

    @GET
    public Iterable<Pothole> findPotholes(@QueryParam("location") String location) {
            Iterable<Pothole> results = null;
            if(location != null){
                results = potholeCollection.find(DBQuery.is("location",location));
            }else{
                results = potholeCollection.find();
            }
            return results;
    }

    @POST
    public Pothole createPothole(Pothole pothole){
            WriteResult<Pothole, String> result =  potholeCollection.insert(pothole);
            return result.getSavedObject();
    }

    @GET
    @Path("/{id}")
    public Optional<Pothole> findPotholeById(@PathParam("id") String id) {
        Pothole pothole = potholeCollection.findOneById(id);
        return Optional.fromNullable(pothole);
    }

    @DELETE
    @Path("/{id}")
    public Response deletePothole(@PathParam("id") String id) {
        Pothole pothole = potholeCollection.findOneById(id);

        if(pothole == null){
            return Response.status(Response.Status.BAD_REQUEST).entity("Pothole does not exist.").build();
        } else {
            potholeCollection.removeById(id);
            return Response.ok().build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateContact(@PathParam("id") String id, Pothole pothole) {

        potholeCollection.updateById(id, pothole);
        return Response.ok(pothole).build();
    }
}
