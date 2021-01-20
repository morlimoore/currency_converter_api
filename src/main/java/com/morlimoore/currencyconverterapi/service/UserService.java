package com.morlimoore.currencyconverterapi.service;

import com.morlimoore.currencyconverterapi.entities.User;

public interface UserService {

    User findUserById(Long userId);

    User findUserByUsername(String username);
}