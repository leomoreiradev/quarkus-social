package io.github.leomoreiradev.quarkussocial.rest;


import io.github.leomoreiradev.quarkussocial.domain.model.User;
import io.github.leomoreiradev.quarkussocial.domain.repository.UserRepository;
import io.github.leomoreiradev.quarkussocial.rest.dto.CreateUserRequest;
import io.github.leomoreiradev.quarkussocial.rest.dto.UpdateUserRequest;
import io.quarkus.hibernate.orm.panache.PanacheQuery;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    //Declarando a dependencia a ser usada
    private UserRepository userRepository;

    @Inject //Injetando a dependencia userRepository
    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @POST
    @Transactional //Essa anotation é necessaria para abrir uma transação no BD
    public Response createUser(CreateUserRequest userRequest){

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        //Salvando user com userRepository
        userRepository.persist(user);

        //Retornando o user salvo
        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        //Listando users
        PanacheQuery<User> query = userRepository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id){
        //Buscando pelo id
        User user = userRepository.findById(id);
        if(user != null) {
          //Deletando user
          userRepository.delete(user);
          return Response.ok(Response.Status.NO_CONTENT).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }



    /*
    * No update Quando há um contexto transacional e a entidade é pega dentro desse contexto
    * qualquer alteração que for feita nessa entidade será comitado quando o metodo for finalizado
    * não precida do userRepository.update
    * */
    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UpdateUserRequest updateUserRequest){
        User user = userRepository.findById(id);
        if(user != null) {
            user.setName(updateUserRequest.getName());
            user.setAge(updateUserRequest.getAge());
            return Response.ok(user).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}

