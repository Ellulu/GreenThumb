package com.helmo.greenThumb.services;

import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.infrastructures.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
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

    public boolean isFollowing(String userId, String targetUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFollowing().stream()
                .anyMatch(subscription -> subscription.getUid().equals(targetUserId));
    }

    public void followUser(String currentUserId, String userIdToFollow) {
        User currentUser = userRepository.findById(currentUserId).orElse(null);
        User userToFollow = userRepository.findById(userIdToFollow)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!currentUser.getFollowing().contains(userToFollow)) {
            currentUser.getFollowing().add(userToFollow);
            userRepository.save(currentUser);
        }
    }

    public void unfollowUser(String currentUserId, String userIdToUnfollow) {
        User currentUser = userRepository.findById(currentUserId).orElse(null);
        User userToUnfollow = userRepository.findById(userIdToUnfollow)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.getFollowing().remove(userToUnfollow);
        userRepository.save(currentUser);
    }

    public boolean isAdmin(String currentUserId) {
        User user = userRepository.findById(currentUserId).orElse(null);

        if (user == null || !user.isAdmin()) return false;

        return true;
    }

}
