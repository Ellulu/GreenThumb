package com.helmo.greenThumb.services;


import com.helmo.greenThumb.infrastructures.NoteRepository;
import com.helmo.greenThumb.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService  {
    private final NoteRepository noteRepository;

    @Autowired
    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public Note saveNote(Note note) {
        return noteRepository.save(note);
    }
}
