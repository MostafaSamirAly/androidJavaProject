package com.iti.mishwary.ui.notes;

import com.iti.mishwary.Models.Note;

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
