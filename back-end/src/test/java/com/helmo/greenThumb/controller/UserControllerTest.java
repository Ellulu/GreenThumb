package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.services.FirebaseService;
import com.helmo.greenThumb.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private FirebaseService firebaseService;

    @MockBean
    private FirebaseToken firebaseToken;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        Mockito.when(firebaseToken.getUid()).thenReturn("testUser");
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getAllUsers() throws Exception {
        User user1 = new User();
        user1.setUid("1");

        User user2 = new User();
        user2.setUid("2");

        List<User> users = Arrays.asList(user1, user2);

        Mockito.when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uid").value("1"))
                .andExpect(jsonPath("$[1].uid").value("2"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getUserById() throws Exception {
        User user = new User();
        user.setUid("1");

        Mockito.when(userService.getUserById("1")).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uid").value("1"));
    }


    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void followUser() throws Exception {
        Mockito.doNothing().when(userService).followUser("testUser", "2");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/2/follow")
                        .requestAttr("firebaseToken", firebaseToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User followed"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void unfollowUser() throws Exception {
        Mockito.doNothing().when(userService).unfollowUser("testUser", "2");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/2/unfollow")
                        .requestAttr("firebaseToken", firebaseToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("User unfollowed"));
    }
}