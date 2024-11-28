package com.helmo.greenThumb.infrastructures;


import com.helmo.greenThumb.model.Note;

import com.helmo.greenThumb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {


}

