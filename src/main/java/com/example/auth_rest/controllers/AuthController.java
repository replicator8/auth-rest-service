package com.example.auth_rest.controllers;

import com.example.auth_rest.service.AuthService;
import com.example.demo.dto.*;
import com.example.demo.endpoints.AuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController implements AuthApi {

    private AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void signUp(AuthRequest request) {
        authService.signUp(request);
    }

    @Override
    public AuthResponse signIn(AuthRequest body) {
        return authService.signIn(body);
    }

    @Override
    public StatusResponse healthCheck() {
        return authService.healthCheck();
    }

    @Override
    public void deleteAccount(AuthRequest request) {
        authService.deleteAccount(request);
    }

    @Override
    public List<AuthData> getAllUsers() {
        return authService.findAll();
    }

    @Override
    public PagedResponse<AuthData> getAllBooks(int age, int page, int size) {
        return authService.findAllUsers(age, page, size);
    }
}
