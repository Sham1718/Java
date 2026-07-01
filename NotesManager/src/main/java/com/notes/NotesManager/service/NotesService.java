package com.notes.NotesManager.service;

import com.notes.NotesManager.dto.NoteDto;
import com.notes.NotesManager.model.Note;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class NotesService {

    private final List<Note> notes=new ArrayList<>();

    private int i=0;

    public void addNote(NoteDto note){
        Note note1=new Note(++i,note.getTitle(), note.getDescription() );
        notes.add(note1);
    }

    public Note updateNote(int id, NoteDto dto
    ){
        for (Note note : notes) {
            if (note.getId() == id) {
                note.setTitle(dto.getTitle());
                note.setDescription(dto.getDescription());
                return note;
            }
        }
        return null;
    }

    public List<Note> showAllNote(){
        return notes;
    }

    public void deleteNOte(int id){
        notes.removeIf(note -> note.getId() == id);
    }

    public Note showByTitle(String title){
       return notes.stream()
               .filter(note -> note.getTitle().equalsIgnoreCase(title))
               .findFirst()
               .orElse(null);
    }

}
