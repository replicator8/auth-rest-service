package com.example.auth_rest.controllers;

import com.example.auth_rest.service.AuthService;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.StatusResponse;
import com.example.demo.endpoints.AuthApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
}
