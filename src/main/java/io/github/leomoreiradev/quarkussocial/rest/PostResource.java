package io.github.leomoreiradev.quarkussocial.rest;


import io.github.leomoreiradev.quarkussocial.domain.model.Post;
import io.github.leomoreiradev.quarkussocial.domain.model.User;
import io.github.leomoreiradev.quarkussocial.domain.repository.PostRepository;
import io.github.leomoreiradev.quarkussocial.domain.repository.UserRepository;
import io.github.leomoreiradev.quarkussocial.rest.dto.CreatePostRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepository;
    private PostRepository postRepository;

    @Inject
    public PostResource(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, CreatePostRequest createPostRequest) {
        User user = userRepository.findById(userId);

        if(user == null) {
            return Response.status( Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(createPostRequest.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }


    @GET
    public Response listPosts(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if(user == null) {
            return Response.status( Response.Status.NOT_FOUND).build();
        }
        return Response.ok().build();
    }
}