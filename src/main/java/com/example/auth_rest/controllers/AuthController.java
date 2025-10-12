package com.example.auth_rest.controllers;

import com.example.auth_rest.assemblers.AuthModelAssembler;
import com.example.auth_rest.service.AuthService;
import com.example.demo.dto.*;
import com.example.demo.endpoints.AuthApi;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class AuthController implements AuthApi {

    private final AuthService authService;
    private final AuthModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<AuthResponse> pagedResourcesAssembler;

    public AuthController(AuthService authService, AuthModelAssembler userModelAssembler, PagedResourcesAssembler<AuthResponse> pagedResourcesAssembler) {
        this.authService = authService;
        this.userModelAssembler = userModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Override
    public ResponseEntity<EntityModel<AuthResponse>> signUp(AuthRequest request) {
        AuthResponse created = authService.signUp(request);
        EntityModel<AuthResponse> model = userModelAssembler.toModel(created);
        return ResponseEntity
                .created(model.getRequiredLink("self").toUri())
                .body(model);
    }

    @Override
    public EntityModel<AuthResponse> signIn(AuthRequest body) {
        AuthResponse user = authService.signIn(body);
        return userModelAssembler.toModel(user);
    }

    @Override
    public EntityModel<StatusResponse> healthCheck() {
        return EntityModel.of(authService.healthCheck());
    }

    @Override
    public void deleteAccount(AuthRequest request) {
        authService.deleteAccount(request);
    }

    @Override
    public CollectionModel<EntityModel<AuthResponse>> getAllUsers() {
        List<AuthResponse> users = authService.findAll();
        return userModelAssembler.toCollectionModel(users);
    }

    @Override
    public EntityModel<AuthResponse> getUserById(Long id) {
        AuthResponse user = authService.findById(id);
        return userModelAssembler.toModel(user);
    }

    @Override
    public PagedModel<EntityModel<AuthResponse>> getAllUsersWithPagination(int age, int page, int size) {
        PagedResponse<AuthResponse> pagedResponse = authService.findAllUsers(age, page, size);
        org.springframework.data.domain.Page<AuthResponse> userPage = new org.springframework.data.domain.PageImpl<>(
                pagedResponse.content(),
                org.springframework.data.domain.PageRequest.of(page, size),
                pagedResponse.totalElements()
        );
        return pagedResourcesAssembler.toModel(userPage, userModelAssembler);
    }
}
