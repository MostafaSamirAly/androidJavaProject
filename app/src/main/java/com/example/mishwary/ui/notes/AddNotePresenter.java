package com.example.mishwary.ui.notes;

import androidx.annotation.NonNull;

import com.example.mishwary.Models.Note;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddNotePresenter implements AddNoteContract.AddNotePresenter {
    AddNoteContract.AddNoteView ref;
    private String tripId;

    public AddNotePresenter(AddNoteContract.AddNoteView ref, String tripId) {

        this.ref = ref;
        this.tripId = tripId;
    }


    @Override
    public void addNote(Note note) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notes").child(note.getTripId());
        note.setId(reference.push().getKey());
        reference.child(note.getId()).setValue(note);
    }

    @Override
    public void getAllNote() {
        final List<Note> notes = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notes").child(tripId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    notes.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Note note = data.getValue(Note.class);
                        notes.add(note);
                    }
                }
                if (notes.size() > 0) {
                    ref.displayNotes(notes);
                }else {
                    ref.displayNoNotes();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
