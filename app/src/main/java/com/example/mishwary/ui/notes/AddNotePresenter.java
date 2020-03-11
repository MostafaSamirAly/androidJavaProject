package com.example.mishwary.ui.notes;

import com.example.mishwary.Models.Note;

public class AddNotePresenter implements AddNoteContract.AddNotePresenter {
    AddNoteContract.AddNoteView ref;
    public AddNotePresenter( AddNoteContract.AddNoteView ref) {
        this.ref = ref;
    }


    @Override
    public void addNote(Note note) {

    }

    @Override
    public void getAllNote() {

    }

}
