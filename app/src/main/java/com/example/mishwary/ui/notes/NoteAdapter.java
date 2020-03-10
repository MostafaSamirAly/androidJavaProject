package com.example.mishwary.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mishwary.Models.Note;
import com.example.mishwary.Models.Trip;
import com.example.mishwary.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<Note> Notes;

    public NoteAdapter(Context context, List  Notes) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this. Notes =  Notes;
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView NoteDesc;
        private Button deleteBtn;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            NoteDesc = itemView.findViewById(R.id.NoteDesc);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        holder.NoteDesc.setText(Notes.get(position).getDescription());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFromNotes(Notes.get(position));
            }
        });

    }
    private void removeFromNotes(Note note) {

    }

    @Override
    public int getItemCount() {
        return Notes.size();
    }
}
