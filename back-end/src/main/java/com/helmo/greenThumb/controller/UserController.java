package com.helmo.greenThumb.controller;

import com.google.firebase.auth.UserRecord;
import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.services.FirebaseService;
import com.helmo.greenThumb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final FirebaseService firebaseService;

    @Autowired
    public UserController(UserService userService, FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRecord> getUserById(@PathVariable("id") String id) {
        UserRecord user = firebaseService.getUserByUid(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
