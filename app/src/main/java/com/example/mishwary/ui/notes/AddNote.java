package com.example.mishwary.ui.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

import com.example.mishwary.Models.Note;
import com.example.mishwary.Models.User;
import com.example.mishwary.R;

import java.util.List;

public class AddNote extends AppCompatActivity implements AddNoteContract.AddNoteView {
    MultiAutoCompleteTextView Desc;
    Button addNote;
    String tripId;
    AddNoteContract.AddNotePresenter addNotePresenter;
    NoteAdapter noteAdapter;
    RecyclerView Notes_RecycleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Desc = findViewById(R.id.NoteDesc);
        addNote = findViewById(R.id.Add_Note);
        Notes_RecycleView = findViewById(R.id.Note_recyclerview);
        Intent intent = getIntent();
        tripId = intent.getStringExtra(" tripId");
        addNotePresenter = new AddNotePresenter(this);
        Note addedNote = new Note(null, Desc.getText().toString(),tripId);
        addNotePresenter.addNote(addedNote);


    }

    @Override
    public void displayNotes(List<Note> Notes) {
        noteAdapter = new NoteAdapter(this,Notes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Notes_RecycleView.setLayoutManager(layoutManager);
        Notes_RecycleView.setHasFixedSize(true);
        Notes_RecycleView.setAdapter( noteAdapter );

    }
}