package com.example.auth_rest.storage;

import com.example.demo.dto.AuthData;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryStorage {
    public final Map<Long, AuthData> users = new ConcurrentHashMap<>();
    public final AtomicLong userSequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // Создаем несколько пользователей
        AuthData user1 = new AuthData(userSequence.incrementAndGet(), "Игорь", "Иванов", "test1@mail.ru", "1234567890", 22, LocalDateTime.now());
        AuthData user2 = new AuthData(userSequence.incrementAndGet(), "Григорий", "Смирнов", "test2@mail.ru", "123456789", 28, LocalDateTime.now());
        users.put(user1.id(), user1);
        users.put(user2.id(), user2);
    }
}
