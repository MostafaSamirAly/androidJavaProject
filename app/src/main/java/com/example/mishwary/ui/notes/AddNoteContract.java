package com.example.mishwary.ui.notes;

import com.example.mishwary.Models.Note;

import java.util.List;

public interface AddNoteContract  {
    public interface AddNoteView{
        void displayNotes(List<Note> Notes);
        void displayNoNotes();

    }
    public interface AddNotePresenter{
        void addNote(Note note);
        void getAllNote();
    }
}
