package com.example.notification.service.impl;

import com.example.notification.model.entity.User;
import com.example.notification.repository.UserRepository;
import com.example.notification.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteByChatId(Long chatId) {
        userRepository.deleteById(chatId);
    }

    @Override
    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }
}