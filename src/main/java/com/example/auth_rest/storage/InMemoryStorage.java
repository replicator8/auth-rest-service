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
        AuthData user3 = new AuthData(userSequence.incrementAndGet(), "Алексей", "Петров", "test3@mail.ru", "1234567891", 25, LocalDateTime.now());
        AuthData user4 = new AuthData(userSequence.incrementAndGet(), "Елена", "Соколова", "test4@mail.ru", "1234567892", 31, LocalDateTime.now());
        AuthData user5 = new AuthData(userSequence.incrementAndGet(), "Дмитрий", "Кузнецов", "test5@mail.ru", "1234567893", 27, LocalDateTime.now());
        AuthData user6 = new AuthData(userSequence.incrementAndGet(), "Ольга", "Попова", "test6@mail.ru", "1234567894", 29, LocalDateTime.now());
        AuthData user7 = new AuthData(userSequence.incrementAndGet(), "Сергей", "Волков", "test7@mail.ru", "1234567895", 24, LocalDateTime.now());
        AuthData user8 = new AuthData(userSequence.incrementAndGet(), "Анна", "Морозова", "test8@mail.ru", "1234567896", 33, LocalDateTime.now());
        AuthData user9 = new AuthData(userSequence.incrementAndGet(), "Андрей", "Новиков", "test9@mail.ru", "1234567897", 26, LocalDateTime.now());
        AuthData user10 = new AuthData(userSequence.incrementAndGet(), "Мария", "Лебедева", "test10@mail.ru", "1234567898", 30, LocalDateTime.now());
        AuthData user11 = new AuthData(userSequence.incrementAndGet(), "Илья", "Козлов", "test11@mail.ru", "1234567899", 23, LocalDateTime.now());
        AuthData user12 = new AuthData(userSequence.incrementAndGet(), "Мария", "Макарова", "test12@mail.ru", "1234567800", 35, LocalDateTime.now());
        AuthData user13 = new AuthData(userSequence.incrementAndGet(), "Владимир", "Григорьев", "test13@mail.ru", "1234567801", 28, LocalDateTime.now());
        AuthData user14 = new AuthData(userSequence.incrementAndGet(), "Мария", "Фёдорова", "test14@mail.ru", "1234567802", 32, LocalDateTime.now());
        AuthData user15 = new AuthData(userSequence.incrementAndGet(), "Павел", "Семёнов", "test15@mail.ru", "1234567803", 27, LocalDateTime.now());
        AuthData user16 = new AuthData(userSequence.incrementAndGet(), "Мария", "Романова", "test16@mail.ru", "1234567804", 29, LocalDateTime.now());
        AuthData user17 = new AuthData(userSequence.incrementAndGet(), "Максим", "Орлов", "test17@mail.ru", "1234567805", 25, LocalDateTime.now());
        AuthData user18 = new AuthData(userSequence.incrementAndGet(), "Юлия", "Сергеева", "test18@mail.ru", "1234567806", 31, LocalDateTime.now());
        AuthData user19 = new AuthData(userSequence.incrementAndGet(), "Артём", "Андреев", "test19@mail.ru", "1234567807", 26, LocalDateTime.now());
        AuthData user20 = new AuthData(userSequence.incrementAndGet(), "Светлана", "Михайлова", "test20@mail.ru", "1234567808", 34, LocalDateTime.now());

        users.put(user1.getId(), user1);
        users.put(user2.getId(), user2);
        users.put(user3.getId(), user3);
        users.put(user4.getId(), user4);
        users.put(user5.getId(), user5);
        users.put(user6.getId(), user6);
        users.put(user7.getId(), user7);
        users.put(user8.getId(), user8);
        users.put(user9.getId(), user9);
        users.put(user10.getId(), user10);
        users.put(user11.getId(), user11);
        users.put(user12.getId(), user12);
        users.put(user13.getId(), user13);
        users.put(user14.getId(), user14);
        users.put(user15.getId(), user15);
        users.put(user16.getId(), user16);
        users.put(user17.getId(), user17);
        users.put(user18.getId(), user18);
        users.put(user19.getId(), user19);
        users.put(user20.getId(), user20);

        System.out.println("test users generated!");
    }
}
