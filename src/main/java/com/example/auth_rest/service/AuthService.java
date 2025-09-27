package com.example.auth_rest.service;

import com.example.auth_rest.storage.InMemoryStorage;
import com.example.demo.dto.AuthData;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.StatusResponse;
import com.example.demo.exceptions.IncorrectPasswordException;
import com.example.demo.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final InMemoryStorage storage;

    public AuthService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public AuthData findById(Long id) {
        return Optional.ofNullable(storage.users.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
    }

    public boolean checkEmail(String email) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().email())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkPasswordByEmail(String email, String password) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().email())) {
                if (password.equals(entry.getValue().password())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Long getIdByEmail(String email) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().email())) {
                return entry.getValue().id();
            }
        }
        return 0L;
    }

    public void signUp(AuthRequest request) {
        System.out.print("SIGN UP: ");
        System.out.println(request);
        long id = storage.userSequence.incrementAndGet();
        AuthData user = new AuthData(id, "", "", request.email(), request.password(), 0, LocalDateTime.now());
        storage.users.put(id, user);
    }

    public AuthResponse signIn(AuthRequest request) {
        System.out.print("SIGN IN: ");
        System.out.println(request);
        String email = request.email();
        String password = request.password();

        if (!checkEmail(email)) throw new ResourceNotFoundException("Email", request.email());
        if (!checkPasswordByEmail(email, password)) throw new IncorrectPasswordException("Incorrect");

        Long id = getIdByEmail(email);
        AuthData data = storage.users.get(id);
        return new AuthResponse(id, data.firstName(), data.lastName(), data.age(), data.createdAt());
    }

    public void deleteAccount(AuthRequest request) {
        System.out.println(request);
        System.out.println("delete account");

        String email = request.email();
        String password = request.password();
        if (!checkEmail(email)) throw new ResourceNotFoundException("Email", email);
        if (!checkPasswordByEmail(email, password)) throw new IncorrectPasswordException("Password");
        Long id = getIdByEmail(request.email());

        storage.users.remove(id);
    }

    public StatusResponse healthCheck() {
        if (storage.users.containsKey(1L)) {
            return new StatusResponse("success", "UP");
        } else {
            return new StatusResponse("error", "DOWN");
        }
    }
}
