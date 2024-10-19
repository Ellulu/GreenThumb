package com.helmo.greenThumb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helmo.greenThumb.model.Note;
import com.helmo.greenThumb.services.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createNote() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setTitle("Test Note");
        note.setContent("This is a test note.");

        Mockito.when(noteService.saveNote(Mockito.any(Note.class))).thenReturn(note);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(note)))
                .andExpect(status().isCreated())
                .andExpect(content().string("La note a bien été créée"));
    }

    @Test
    void getAllNotes() throws Exception {
        Note note1 = new Note();
        note1.setId(1L);
        note1.setTitle("Test Note 1");
        note1.setContent("test note.");

        Note note2 = new Note();
        note2.setId(2L);
        note2.setTitle("Test Note 2");
        note2.setContent("test note note.");

        List<Note> notes = Arrays.asList(note1, note2);

        Mockito.when(noteService.findAll()).thenReturn(notes);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Note 1"))
                .andExpect(jsonPath("$[0].content").value("test note."))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Test Note 2"))
                .andExpect(jsonPath("$[1].content").value("test note note."));
    }
}