package com.example.auth_rest.service;

import com.example.auth_rest.config.RabbitMQConfig;
import com.example.auth_rest.storage.InMemoryStorage;
import com.example.demo.dto.*;
import com.example.demo.exceptions.EmailAlreadyExistsException;
import com.example.demo.exceptions.IncorrectPasswordException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.netflix.graphql.dgs.InputArgument;
import events.UserCreateEvent;
import events.UserDeleteEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AuthService {
    private final RabbitTemplate rabbitTemplate;
    private final InMemoryStorage storage;

    public AuthService(InMemoryStorage storage, RabbitTemplate rabbitTemplate) {
        this.storage = storage;
        this.rabbitTemplate = rabbitTemplate;
    }

    public AuthResponse findById(Long id) {
        AuthData data = Optional.ofNullable(storage.users.get(id))
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return toAuthResponse(data);
    }

    public List<AuthResponse> findAll() {
        return storage.users.values().stream()
                .map(this::toAuthResponse)
                .toList();
    }

    public List<AuthResponse> findAll(Long id, Integer age, String email, String firstName) {
        Stream<AuthData> usersStream = storage.users.values().stream()
                .sorted((b1, b2) -> b2.getId().compareTo(b2.getId()));

        if (id != null) {
            usersStream = usersStream.filter(book -> book.getId() != null && book.getId().equals(id));
        }

        if (age != null) {
            usersStream = usersStream.filter(book -> book.getAge() >= 0 && book.getAge() == age);
        }

        if (email != null) {
            usersStream = usersStream.filter(book -> book.getEmail() != null && book.getEmail().equalsIgnoreCase(email));
        }

        if (firstName != null) {
            usersStream = usersStream.filter(book -> book.getFirstName() != null && book.getFirstName().equalsIgnoreCase(firstName));
        }

        return usersStream.map(this::toAuthResponse).toList();
    }

    public Long getIdByEmail(String email) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().getEmail())) {
                return entry.getValue().getId();
            }
        }
        return 0L;
    }

    public AuthResponse signUp(AuthRequest request) {
        System.out.print("SIGN UP: ");
        System.out.println(request);

        String email = request.email();
        String password = request.password();

        if (checkEmail(email)) throw new EmailAlreadyExistsException(email);

        long id = storage.userSequence.incrementAndGet();
        AuthData user = new AuthData(id, "", "", email, password, 0, LocalDateTime.now());
        storage.users.put(id, user);

        UserCreateEvent event = new UserCreateEvent(id, email);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_USER_CREATED, event);

        return new AuthResponse(id, "", 0, "", LocalDateTime.now());
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
        return new AuthResponse(id, data.getFirstName(), data.getAge(), data.getLastName(), data.getCreatedAt());
    }

    public Long deleteAccount(AuthRequest request) {
        System.out.print("delete account");
        System.out.println(request);

        String email = request.email();
        String password = request.password();

        if (!checkEmail(email)) throw new ResourceNotFoundException("Email", email);
        if (!checkPasswordByEmail(email, password)) throw new IncorrectPasswordException("Password");
        Long id = getIdByEmail(request.email());

        UserDeleteEvent event = new UserDeleteEvent(id, email);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY_USER_DELETED, event);

        storage.users.remove(id);
        return id;
    }

    public StatusResponse healthCheck() {
        if (storage.users.containsKey(1L)) {
            return new StatusResponse("success", "UP");
        } else {
            return new StatusResponse("error", "DOWN");
        }
    }

    public PagedResponse<AuthResponse> findAllUsers(int age, int page, int size) {
        Stream<AuthData> stream = storage.users.values().stream()
                .sorted(Comparator.comparing(AuthData::getId));

        if (age != 0) {
            stream = stream.filter(user -> user.getAge() == age);
        }

        List<AuthResponse> allResponses = stream
                .map(this::toAuthResponse)
                .toList();

        int total = allResponses.size();
        int from = page * size;
        int to = Math.min(from + size, total);
        List<AuthResponse> pageContent = from < total ? allResponses.subList(from, to) : List.of();

        int totalPages = (int) Math.ceil((double) total / size);
        boolean last = page >= totalPages - 1;

        return new PagedResponse<>(pageContent, page, size, total, totalPages, last);
    }

    private boolean checkEmail(String email) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().getEmail())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPasswordByEmail(String email, String password) {
        for (Map.Entry<Long, AuthData> entry: storage.users.entrySet()) {
            if (email.equals(entry.getValue().getEmail())) {
                if (password.equals(entry.getValue().getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    private AuthResponse toAuthResponse(AuthData data) {
        return new AuthResponse(
                data.getId(),
                data.getFirstName(),
                data.getAge(),
                data.getLastName(),
                data.getCreatedAt()
        );
    }
}
