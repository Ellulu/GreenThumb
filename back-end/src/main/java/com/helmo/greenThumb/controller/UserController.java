package com.helmo.greenThumb.controller;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.StorageClient;
import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.services.UserService;
import com.helmo.greenThumb.utils.FileValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        User user = userService.getUserById(id);
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

    @PostMapping("/upload")
    public void saveProfilePicture(@RequestAttribute("firebaseToken") FirebaseToken token, @RequestParam("file") MultipartFile file) {
        var uid = token.getUid();

        if(FileValidator.validateImage(file)) {
            try {
                Bucket bucket = StorageClient.getInstance().bucket("greenthumb-54c99.firebasestorage.app");
                String blobName = "profile_pictures/" + uid + "/" + new Date().getTime() + "-" + file.getOriginalFilename();
                Blob blob = bucket.create(blobName, file.getInputStream());
                
                String downloadUrl = "https://storage.googleapis.com/" + bucket.getName() + "/" + blob.getName();

                UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid).setPhotoUrl(downloadUrl);
                
                FirebaseAuth.getInstance().updateUser(request);
            } catch(FileNotFoundException ex) {
                System.out.println("File Not Found: " + ex.getMessage());
            } catch(FirebaseAuthException ex) {
                System.out.println("Firebase Auth: " + ex.getMessage());
            } catch(IOException ex) {
                System.out.println("Input Output: " + ex.getMessage());
            }
        } else {
            System.out.println("Invalid file");
        }
    } 

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
