package com.example.notification.service;

import com.example.notification.model.entity.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    void deleteByChatId(Long chatId);

    Optional<User> findByChatId(Long chatId);
}