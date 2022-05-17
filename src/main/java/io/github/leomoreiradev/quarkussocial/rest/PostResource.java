package io.github.leomoreiradev.quarkussocial.rest;


import io.github.leomoreiradev.quarkussocial.domain.model.Post;
import io.github.leomoreiradev.quarkussocial.domain.model.User;
import io.github.leomoreiradev.quarkussocial.domain.repository.PostRepository;
import io.github.leomoreiradev.quarkussocial.domain.repository.UserRepository;
import io.github.leomoreiradev.quarkussocial.rest.dto.CreatePostRequest;
import io.github.leomoreiradev.quarkussocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

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

        return Response.status(Response.Status.CREATED.getStatusCode())
                .entity(PostResponse.fromEntity(post))
                .build();
    }


    @GET
    public Response listPosts(@PathParam("userId") Long userId) {
        User user = userRepository.findById(userId);

        if(user == null) {
            return Response.status( Response.Status.NOT_FOUND).build();
        }

        PanacheQuery<Post> query = postRepository
                .find("user", Sort.by("dateTime", Sort.Direction.Descending) ,user);

        var listPosts = query.list();

        var postResponseList = listPosts
                                                    .stream()
                                                    .map(post -> PostResponse.fromEntity(post))
                                                    .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
    }
}
