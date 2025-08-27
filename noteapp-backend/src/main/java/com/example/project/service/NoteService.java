package com.example.project.service;

import com.example.project.entity.Note;
import java.util.List;

public interface NoteService {
    List<Note> findByUserId(Long userId);
    Note findById(Long id);
    Note create(Note note);
    Note update(Note note);
    boolean delete(Long id);
}