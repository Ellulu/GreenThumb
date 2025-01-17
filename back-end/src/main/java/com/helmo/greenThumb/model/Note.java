package com.helmo.greenThumb.model;

import jakarta.persistence.*;

@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    private String title;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;





    public Note( String title, String content ) {
        this.title = title;
        this.content = content;
    }

    public Note() {

    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getContent() {
        return content;
    }
    public String getTitle() {
        return title;
    }
    public void setContent( String content ) {
        this.content = content;
    }
    public void setTitle( String title ) {
        this.title = title;
    }


    public void setId(long l) {
        this.id = l;
    }
    public Long getId() {
        return id;
    }
}
