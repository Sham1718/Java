package com.notes.NotesManager.controller;

import com.notes.NotesManager.dto.NoteDto;
import com.notes.NotesManager.model.Note;
import com.notes.NotesManager.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/notes")
public class Controller {
    private final NotesService service;

    public Controller(NotesService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<List<Note>> getAllNotes(){
        List<Note> notes=service.showAllNote();
        return ResponseEntity.of(Optional.ofNullable(notes));
    }

    @PostMapping("/")
    public ResponseEntity<String> addNote(
            @RequestBody NoteDto dto
            ){
        service.addNote(dto);
        return ResponseEntity.ok("Note added");

    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(
            @PathVariable int id,
            @RequestBody NoteDto Dto
    ){
        Note note=service.updateNote(id,Dto);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(
            @PathVariable int id
    ){
        service.deleteNOte(id);
        return ResponseEntity.ok("deleted the note");
    }

    @GetMapping("/title")
    public ResponseEntity<Note> getByTitle(
            @RequestParam String title
    ){
        Note note = service.showByTitle(title);

        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(note);
    }

}
