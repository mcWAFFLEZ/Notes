package com.example.notes;



import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class EditNoteActivity extends AppCompatActivity {

    EditText editText;
    int index;
    boolean isSentFromItemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        editText = findViewById(R.id.editText);

        isSentFromItemClick = getIntent().getBooleanExtra("sentFromItemClick",false);

        if(isSentFromItemClick) {

            index = getIntent().getIntExtra("listIndex", 0);
            editText.setText(MainActivity.notes.get(index));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(isSentFromItemClick) {
            MainActivity.notes.set(index, editText.getText().toString());
            MainActivity.myArrayAdapter.notifyDataSetChanged();

            MainActivity.saveNotesData( MainActivity.notes);

        } else {
            MainActivity.notes.add(editText.getText().toString());
            MainActivity.myArrayAdapter.notifyDataSetChanged();

            MainActivity.saveNotesData( MainActivity.notes);

        }

        Toast.makeText(this,"Note saved",Toast.LENGTH_SHORT).show();
    }
}
