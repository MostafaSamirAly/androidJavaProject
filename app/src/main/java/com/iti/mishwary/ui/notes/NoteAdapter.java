package com.iti.mishwary.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mishwary.Models.Note;
import com.iti.mishwary.R;
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
        private ImageButton deleteBtn;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            NoteDesc = itemView.findViewById(R.id.NoteDesc);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(view);
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
        DatabaseReference removeReference = FirebaseDatabase.getInstance().getReference("notes").child(note.getTripId());
        removeReference.child(note.getId()).removeValue();
    }

    @Override
    public int getItemCount() {
        return Notes.size();
    }
}
