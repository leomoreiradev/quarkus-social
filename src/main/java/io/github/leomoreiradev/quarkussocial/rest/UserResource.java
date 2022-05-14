package io.github.leomoreiradev.quarkussocial.rest;


import io.github.leomoreiradev.quarkussocial.domain.model.User;
import io.github.leomoreiradev.quarkussocial.rest.dto.CreateUserRequest;
import io.github.leomoreiradev.quarkussocial.rest.dto.UpdateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    @Transactional //Essa anotation é necessaria para abrir uma transação no BD
    public Response createUser(CreateUserRequest userRequest){

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        //Salvando user (a entidade user se auto persiste)
        user.persist();

        //Retornando o user salvo
        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        //Listando users
        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id){
        User user = User.findById(id);
        if(user != null) {
          user.delete();
          return Response.ok(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UpdateUserRequest updateUserRequest){
        User user = User.findById(id);
        if(user != null) {
            user.setName(updateUserRequest.getName());
            user.setAge(updateUserRequest.getAge());

            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

