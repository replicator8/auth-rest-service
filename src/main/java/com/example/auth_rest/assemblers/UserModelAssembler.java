package com.example.auth_rest.assemblers;

import com.example.auth_rest.controllers.AuthController;
import com.example.demo.dto.AuthData;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<AuthData, EntityModel<AuthData>> {
    @Override
    public EntityModel<AuthData> toModel(AuthData user) {
        return EntityModel.of(user,
              WebMvcLinkBuilder.linkTo(methodOn(AuthController.class).getAllBooks(0, 0, 10)).withRel("collection")
        );
    }
}