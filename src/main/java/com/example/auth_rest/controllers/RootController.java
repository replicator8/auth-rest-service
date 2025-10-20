package com.example.auth_rest.controllers;

import com.example.demo.dto.AuthRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/")
public class RootController {
    @GetMapping
    public RepresentationModel<?> getRoot() {
        RepresentationModel<?> rootModel = new RepresentationModel<>();
        rootModel.add(
                linkTo(methodOn(AuthController.class).signIn(new AuthRequest("test@mail.ru", "123456"))).withRel("Sign In"),
                linkTo(methodOn(AuthController.class).signUp(new AuthRequest("test@mail.ru", "123456"))).withRel("Sign Up"));
        rootModel.add(Link.of("/swagger-ui.html", "documentation"));
        return rootModel;
    }
}
