package com.helmo.greenThumb.infrastructures;


import com.helmo.greenThumb.model.Note;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

}

