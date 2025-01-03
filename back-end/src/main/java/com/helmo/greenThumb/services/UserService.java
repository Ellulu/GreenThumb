package com.helmo.greenThumb.services;

import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.infrastructures.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    public void subscribe(String currentUserId, String targetUserId) {
        User currentUser = userRepository.findById(currentUserId).orElseThrow();
        User targetUser = userRepository.findById(targetUserId).orElseThrow();
        currentUser.getSubscribers().add(targetUser);
        userRepository.save(currentUser);
    }

    public void unsubscribe(String currentUserId, String targetUserId) {
        User currentUser = userRepository.findById(currentUserId).orElseThrow();
        User targetUser = userRepository.findById(targetUserId).orElseThrow();
        currentUser.getSubscribers().remove(targetUser);
        userRepository.save(currentUser);
    }


}
