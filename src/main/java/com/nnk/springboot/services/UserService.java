package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;

import java.util.List;

public interface UserService {
    //CREATE
    User addUser(User user);

    //READ
    List<User> getAllUsers();

    //UPDATE
    User updateUser(Long id, User user);

    //DELETE
    String deleteUser(Long id);
}
