package com.helmo.greenThumb.services;


import com.helmo.greenThumb.infrastructures.NoteRepository;
import com.helmo.greenThumb.infrastructures.UserRepository;
import com.helmo.greenThumb.model.Note;
import com.helmo.greenThumb.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService  {
    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> findAll() {
        return noteRepository.findAll();
    }

    public void edit(Long id, Note note) {
        Note noteToEdit = noteRepository.findById(id).orElseThrow();
        noteToEdit.setContent(note.getContent());
        noteToEdit.setTitle(note.getTitle());
        noteRepository.save(noteToEdit);
    }

    public void delete(Long id) {
        noteRepository.deleteById(id);
    }

    public List<Note> findAllNoteByUser(String userUid) {
        User finduser = userRepository.findById(userUid).orElse(null);

        if (finduser == null) {
            return Collections.emptyList();
        }
        return noteRepository.findAll().stream()
                .filter(note -> note.getUser().getUid().equals(userUid))
                .collect(Collectors.toList());


    }
}
