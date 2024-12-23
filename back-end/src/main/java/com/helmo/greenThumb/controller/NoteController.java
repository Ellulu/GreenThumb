package com.helmo.greenThumb.controller;
import com.google.firebase.auth.FirebaseToken;
import com.helmo.greenThumb.model.Note;
import com.helmo.greenThumb.model.User;
import com.helmo.greenThumb.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:5173")
public class NoteController {

    @Autowired
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }


    @PostMapping
    public ResponseEntity<String> createNote(   @RequestAttribute("firebaseToken") FirebaseToken token,@RequestBody Note note) {
        noteService.saveNote(token.getUid(),note);
        return ResponseEntity.status(HttpStatus.CREATED).body("La note a bien été créée");
    }

    @GetMapping("/{user}")
    public ResponseEntity<List<Note>> getAllNotes(@RequestAttribute("firebaseToken") FirebaseToken token) {
        List<Note> notes = noteService.findAllNoteByUser(token.getUid());


        return ResponseEntity.ok(notes);
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> editNote(@PathVariable Long id, @RequestBody Note note) {
        noteService.edit(id, note);
        return ResponseEntity.status(HttpStatus.OK).body("La note a bien été modifiée");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {
        noteService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("La note a bien été supprimée");
    }
}
