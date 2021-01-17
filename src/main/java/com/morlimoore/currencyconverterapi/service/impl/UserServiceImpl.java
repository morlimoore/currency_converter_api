package com.morlimoore.currencyconverterapi.service.impl;

import com.morlimoore.currencyconverterapi.entities.User;
import com.morlimoore.currencyconverterapi.exceptions.CustomException;
import com.morlimoore.currencyconverterapi.repositories.UserRepository;
import com.morlimoore.currencyconverterapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User findUserById(Long userId) {
        Optional<User> optional = userRepository.findById(userId);
        User user = optional.orElseThrow(() -> new CustomException("User does not exist", HttpStatus.BAD_REQUEST));
        return user;
    }
}
