package com.example.auth_rest.graphql;

import com.example.auth_rest.service.AuthService;
import com.example.demo.dto.AuthData;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

import java.util.List;
import java.util.Map;

@DgsComponent
public class AuthDataFetcher {

    private final AuthService authService;

    public AuthDataFetcher(AuthService authService) {
        this.authService = authService;
    }

    @DgsQuery
    public List<AuthData> getAllUsers(@InputArgument Long id, @InputArgument Integer age, @InputArgument String email, @InputArgument String firstName) {
        return authService.findAll(id, age, email, firstName);
    }

    @DgsQuery
    public AuthResponse signIn(@InputArgument("body") Map<String, String> body) {
        AuthRequest request = new AuthRequest(body.get("email"), body.get("password"));
        return authService.signIn(request);
    }

    @DgsMutation
    public AuthResponse signUp(@InputArgument("body") Map<String, String> body) {
        AuthRequest request = new AuthRequest(body.get("email"), body.get("password"));
        return authService.signUp(request);
    }

    @DgsMutation
    public Long deleteAccount(@InputArgument("body") Map<String, String> body) {
        AuthRequest request = new AuthRequest(body.get("email"), body.get("password"));
        return authService.deleteAccount(request);
    }
}
