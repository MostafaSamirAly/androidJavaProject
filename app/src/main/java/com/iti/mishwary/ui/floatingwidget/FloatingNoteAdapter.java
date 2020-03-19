package com.iti.mishwary.ui.floatingwidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iti.mishwary.Models.Note;
import com.iti.mishwary.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FloatingNoteAdapter extends RecyclerView.Adapter<FloatingNoteAdapter.NoteViewHolder> {
    LayoutInflater inflater;
    Context context;
    List<Note> Notes;

    public FloatingNoteAdapter(Context context, List  Notes) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this. Notes =  Notes;
    }
    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView NoteDesc;
        private CheckBox checkBox;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            NoteDesc = itemView.findViewById(R.id.NoteDesc);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.floating_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NoteViewHolder holder, final int position) {
        holder.NoteDesc.setText(Notes.get(position).getDescription());
        if(Notes.get(position).isChecked()){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked()){
                    holder.checkBox.setChecked(true);
                    updateNotes(Notes.get(position),true);
                }else{
                    updateNotes(Notes.get(position),false);
                    holder.checkBox.setChecked(false);

                }
            }
        });

    }
    private void updateNotes(Note note,boolean selection) {
        DatabaseReference updateRef = FirebaseDatabase.getInstance().getReference("notes").child(note.getTripId());
        note.setChecked(selection);
        updateRef.child(note.getId()).setValue(note);
    }

    @Override
    public int getItemCount() {
        return Notes.size();
    }
}
