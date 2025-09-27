package com.example.auth_rest.service;

import com.example.auth_rest.storage.InMemoryStorage;
import com.example.demo.dto.*;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.IncorrectPasswordException;
import com.example.demo.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AuthService {
    private final InMemoryStorage storage;

    public AuthService(InMemoryStorage storage) {
        this.storage = storage;
    }

    public List<AuthData> findAll() {
        return storage.users.values().stream().toList();
    }

    public AuthData findById(Long id) {
        return Optional.ofNullable(storage.users.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("Author", id));
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

        String email = request.email();
        String password = request.password();

        if (checkEmail(email)) throw new EmailAlreadyExistsException(email);

        long id = storage.userSequence.incrementAndGet();
        AuthData user = new AuthData(id, "", "", email, password, 0, LocalDateTime.now());
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
        System.out.print("delete account");
        System.out.println(request);

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

    public PagedResponse<AuthData> findAllUsers(int age, int page, int size) {
        // Получаем стрим всех пользователей
        Stream<AuthData> usersStream = storage.users.values().stream()
                .sorted(Comparator.comparing(AuthData::id)); // Сортируем для консистентности

        // Фильтруем, если указан age
        if (age != 0) {
            usersStream = usersStream.filter(user -> user.age() == age);
        }

        List<AuthData> allUsers = usersStream.toList();

        // Выполняем пагинацию
        int totalElements = allUsers.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalElements);

        List<AuthData> pageContent = (fromIndex > toIndex) ? List.of() : allUsers.subList(fromIndex, toIndex);

        return new PagedResponse<>(pageContent, page, size, totalElements, totalPages, page >= totalPages - 1);
    }

    private boolean checkEmail(String email) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().email())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPasswordByEmail(String email, String password) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().email())) {
                if (password.equals(entry.getValue().password())) {
                    return true;
                }
            }
        }
        return false;
    }
}
