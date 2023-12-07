package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRepository.findById(id)
                .map(u ->{
                    u.setFullname(u.getFullname());
                    u.setUsername(u.getUsername());
                    u.setPassword(u.getPassword());
                    return userRepository.save(u);
                } ).orElseThrow(()-> new RuntimeException("User non trouvé !"));
    }

    @Override
    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "user deleted";
    }
}
