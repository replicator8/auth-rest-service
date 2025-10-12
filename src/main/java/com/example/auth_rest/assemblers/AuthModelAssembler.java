package com.example.auth_rest.assemblers;

import com.example.auth_rest.controllers.AuthController;
import com.example.demo.dto.AuthResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AuthModelAssembler implements RepresentationModelAssembler<AuthResponse, EntityModel<AuthResponse>> {

    @Override
    public EntityModel<AuthResponse> toModel(AuthResponse user) {
        return EntityModel.of(user,
                linkTo(methodOn(AuthController.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(AuthController.class).getAllUsers()).withRel("collection")
        );
    }

    @Override
    public CollectionModel<EntityModel<AuthResponse>> toCollectionModel(Iterable<? extends AuthResponse> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities)
                .add(linkTo(methodOn(AuthController.class).getAllUsers()).withSelfRel());
    }
}