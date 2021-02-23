package com.example.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    protected static SharedPreferences mySharedPreferences;
    ListView myListView;
    protected static ArrayAdapter myArrayAdapter;
    protected static ArrayList<String> notes = new ArrayList<>(Arrays.asList("Example note"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySharedPreferences = this.getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);

        getNotesData();

        myListView = findViewById(R.id.myListView);
        myArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        myListView.setAdapter(myArrayAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("listIndex",position);
                intent.putExtra("sentFromItemClick",true);
                startActivity(intent);
            }
        });

        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Are you sure you want to delete note number "+position+"?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                                notes.remove(position);
                                myArrayAdapter.notifyDataSetChanged();
                                saveNotesData(notes);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getGroupId() == 0){
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public static void saveNotesData(ArrayList<String> notes) {

        try {
            mySharedPreferences.edit().putString("notes", ObjectSerializer.serialize(notes)).apply();
            Log.i("notes",(String) ObjectSerializer.serialize(notes));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNotesData() {

        try {
            notes = (ArrayList<String>) ObjectSerializer.deserialize(mySharedPreferences.getString("notes",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
