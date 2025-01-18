package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Note;
import com.helmo.greenThumb.services.NoteService;
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
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NoteService noteService;

    @MockBean
    private FirebaseToken firebaseToken;

    @BeforeEach
    void setUp() {
        Mockito.when(firebaseToken.getUid()).thenReturn("testUser");
    }



    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void getAllNotes() throws Exception {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Note 1");
        note1.setContent("Content 1");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Note 2");
        note2.setContent("Content 2");

        List<Note> notes = Arrays.asList(note1, note2);

        Mockito.when(noteService.findAllNoteByUser(Mockito.anyString())).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notes/get")
                        .requestAttr("firebaseToken", firebaseToken)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void editNote() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Updated Note");
        note.setContent("Updated content.");

        Mockito.doNothing().when(noteService).edit(Mockito.anyLong(), Mockito.any(Note.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("La note a bien été modifiée"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void deleteNote() throws Exception {
        Mockito.doNothing().when(noteService).delete(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/notes/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("La note a bien été supprimée"));
    }
}