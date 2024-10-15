package com.helmo.greenThumb.dto;

import com.helmo.greenThumb.model.Note;

public class NoteDTO {
    private Long id;
    private String title;
    private String content;

    public NoteDTO( Long id, String title, String content ) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public NoteDTO(Note note) {
        this.id = null;
        this.title = note.getTitle();
        this.content = note.getContent();
    }

    public Long getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public String getTitle() {
        return title;
    }
    public void setId( Long id ) {
        this.id = id;
    }
    public void setContent( String content ) {
        this.content = content;
    }
    public void setTitle( String title ) {
        this.title = title;
    }
}
