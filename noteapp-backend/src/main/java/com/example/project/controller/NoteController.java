package com.example.project.controller;

import com.example.project.common.Result;
import com.example.project.entity.Note;
import com.example.project.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @GetMapping("/user/{userId}")
    public Result<List<Note>> getNotesByUserId(@PathVariable Long userId) {
        List<Note> notes = noteService.findByUserId(userId);
        return Result.ok(notes);
    }
    
    @GetMapping("/{id}")
    public Result<Note> getNoteById(@PathVariable Long id) {
        Note note = noteService.findById(id);
        return Result.ok(note);
    }
    
    @PostMapping
    public Result<Note> createNote(@RequestBody Note note) {
        Note createdNote = noteService.create(note);
        return Result.ok(createdNote);
    }
    
    @PutMapping
    public Result<Note> updateNote(@RequestBody Note note) {
        Note updatedNote = noteService.update(note);
        return Result.ok(updatedNote);
    }
    
    @DeleteMapping("/{id}")
    public Result<String> deleteNote(@PathVariable Long id) {
        boolean deleted = noteService.delete(id);
        if (deleted) {
            return Result.ok("删除成功");
        } else {
            return Result.fail("删除失败");
        }
    }
}